package com.example.messagewithme;

import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class food_informatino_activity extends AppCompatActivity {
    TextView food_name,expire_time,food_location,Quantity,Contact_Number;
    ImageView food_image;
    StorageReference imageRef;
    Button ConformationBtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_informatino);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        food_image =findViewById(R.id.row_image);
        food_name=findViewById(R.id.row_title);
        expire_time=findViewById(R.id.expire_time);
        food_location=findViewById(R.id.food_location);
        Quantity=findViewById(R.id.food_quantity);
        ConformationBtn= findViewById(R.id.comformation_btn);
        Contact_Number=findViewById(R.id.Contact_number);
        String imgid = getIntent().getExtras().getString("food_image_name");
        try{
            imageRef= FirebaseStorage.getInstance().getReference("FoodImage/" + imgid);
        }catch(Exception e){

        }
        if(imageRef !=null){
            downloadImage(imageRef);
        }

        food_name.setText(getIntent().getExtras().getString("food_name"));
        expire_time.setText("Expire At : "+getIntent().getExtras().getString("food_expire"));
        food_location.setText("Location : "+getIntent().getExtras().getString("food_location"));
        Quantity.setText("Quantity : "+getIntent().getExtras().getString("quantity"));
        Contact_Number.setText("Contact Number : "+getIntent().getExtras().getString("Contact_number"));
//        Contact_Number.setText("88");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());







    }
    private void downloadImage(StorageReference ref){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Glide.with(food_informatino_activity.this).load(url).into(food_image);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //alert Dialog Box

        ConformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder delDialog = new AlertDialog.Builder(food_informatino_activity.this);
                delDialog.setTitle("Are you sure.");
                delDialog.setMessage("Do you want this item ?\nnote: please save the contact number before this:");
                delDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(food_informatino_activity.this, "Your request has been placed successfully", Toast.LENGTH_LONG).show();
                        finish();
//                        Toast.makeText(food_informatino_activity.this, "item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                delDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                delDialog.show();

               // Toast.makeText(food_informatino_activity.this, "Your request has been placed successfully", Toast.LENGTH_LONG).show();

            }
        });



    }

    }

