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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    String[] items = {"All","Food","Others"};
    RecyclerView recyclerView;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    FloatingActionButton donate_btn;
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





        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.donate_dialog_box);
                food_btn=dialog.findViewById(R.id.food_btn);
//                Book_btn=dialog.findViewById(R.id.Books_btn);
//                Clothes_btn=dialog.findViewById(R.id.Clothes_btn);

                Others_btn=dialog.findViewById(R.id.Others_btn);

                food_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,add_food_activity.class);
                        intent.putExtra("category","food");
                        startActivity(intent);
                    }
                });
                Others_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,add_food_activity.class);
                        intent.putExtra("category","others");
                        startActivity(intent);

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
                    if(!search_editText.getText().toString().toLowerCase().equals("")){
                        foodlist.clear();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("foodDetails");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    foodDetails =snapshot1.getValue(Food_details.class);
                                    if(search_editText.getText().toString().toLowerCase().equals(foodDetails.food_name.toLowerCase())){
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





                startActivity(new Intent(MainActivity.this,Edit_Profile.class));


            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(item=="All"){
                    foodlist.clear();
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
                else if(item=="Food"){
                    foodlist.clear();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("foodDetails");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                foodDetails =snapshot1.getValue(Food_details.class);
                                if(!"".equals(foodDetails.food_expire)){
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
                    foodlist.clear();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("foodDetails");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                foodDetails =snapshot1.getValue(Food_details.class);
                                if("".equals(foodDetails.food_expire)){
                                    foodlist.add(foodDetails);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
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