package com.example.messagewithme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] items = {"Food","Clothes","Books","Others"};
    RecyclerView recyclerView;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    Button donate_btn;
    Button food_btn,Book_btn,Clothes_btn,Others_btn,search_btn;
    ImageView profile_pic_image_view;
    StorageReference imageRef;
    Food_details foodDetails;
    ArrayList<Food_details> foodlist;
    RecyclerAdapter_food adapter;
    EditText search_editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



         search_btn=findViewById(R.id.Search_btn);
         search_editText=findViewById(R.id.search_editext);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        donate_btn=findViewById(R.id.Donate_btn);
        profile_pic_image_view=findViewById(R.id.Profile_pic_image_view);

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);

        autoCompleteTextView.setAdapter(adapterItems);


        //profile image
        try{
            imageRef= FirebaseStorage.getInstance().getReference("images/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        }catch(Exception e){

        }
       if(imageRef !=null){
           downloadImage(imageRef);
       }





        //profile image
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }
        });
        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.donate_dialog_box);
                food_btn=dialog.findViewById(R.id.food_btn);
                Book_btn=dialog.findViewById(R.id.Books_btn);
                Clothes_btn=dialog.findViewById(R.id.Clothes_btn);
                Others_btn=dialog.findViewById(R.id.Others_btn);

                food_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,add_food_activity.class));
                    }
                });
                dialog.show();



            }
        });

        foodDetails = new Food_details();
        foodlist = new ArrayList<Food_details>();
        adapter =new RecyclerAdapter_food(this,foodlist);
        recyclerView.setAdapter(adapter);
        if(search_editText.getText().toString().equals("")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("foodDetails");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        foodDetails =snapshot1.getValue(Food_details.class);
                        foodlist.add(foodDetails);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        //search querrys


            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!search_editText.getText().toString().equals("")){
                        foodlist.clear();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("foodDetails");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    foodDetails =snapshot1.getValue(Food_details.class);
                                    if(search_editText.getText().toString().equals(foodDetails.food_name)){
                                        foodlist.add(foodDetails);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }else{
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                        finish();
                    }


                }
            });

        profile_pic_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delDialog = new AlertDialog.Builder(MainActivity.this);
                delDialog.setTitle("Are you sure ");
                delDialog.setMessage("Do you want to logout");
                delDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,LoginPhoneNumberActivity.class));
                        finish();

//                        Toast.makeText(food_informatino_activity.this, "item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                delDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                delDialog.show();


            }
        });




    }
    private void downloadImage(StorageReference ref){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Glide.with(MainActivity.this).load(url).into(profile_pic_image_view);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}