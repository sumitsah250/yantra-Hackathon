package com.example.messagewithme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messagewithme.Utils.AndroidUtil;
import com.example.messagewithme.Utils.MyUser;
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
import com.google.firebase.storage.UploadTask;

public class add_food_activity extends AppCompatActivity {
    EditText food_name_edt,food_expire_edt,food_location_edt,food_quantity_edt;
    TextView food_image_view;
    Button donate;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    StorageReference storageReference;
    private final int GALLARY_REQ_CODE=1;
    StorageReference imageRef;
    Uri imageUri;
    ProgressBar progressBar;
    TextView categorytext,expiretext;
    MyUser myUser;
    String phonenum="xxxxxx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar =findViewById(R.id.Progress_bar);
        food_name_edt=findViewById(R.id.food_name_edt);
        food_image_view=findViewById(R.id.food_image_view);
        food_expire_edt=findViewById(R.id.food_expire_edt);
        food_location_edt=findViewById(R.id.food_location_edt);
        food_quantity_edt = findViewById(R.id.food_quantity_edt);
        donate=findViewById(R.id.Donate_food_btn);
        categorytext= findViewById(R.id.categorytext);
        expiretext=findViewById(R.id.expiretext);

        Intent intent = getIntent();
        String categ = intent.getStringExtra("category");
        if(categ.equals("food")){
            categorytext.setText("Food");


        }else{
            categorytext.setText("Others");
            expiretext.setVisibility(View.GONE);
            food_expire_edt.setVisibility(View.GONE);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myUser=new MyUser();
                myUser=snapshot.getValue(MyUser.class);
                phonenum=myUser.getPhonenumbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInProgress(true);
                String id = FirebaseAuth.getInstance().getUid()+food_name_edt.getText().toString();

                Food_details foodDetails =new Food_details(food_name_edt.getText().toString(),food_expire_edt.getText().toString(),food_location_edt.getText().toString(),food_quantity_edt.getText().toString(),id,FirebaseAuth.getInstance().getCurrentUser().getUid(),phonenum);

                try {
                    firebaseDatabase.getReference().child("foodDetails").child(id).setValue(foodDetails);
                }catch (Exception e){
                    Toast.makeText(add_food_activity.this   , "sumit1"+""+e, Toast.LENGTH_SHORT).show();
                }




                String fileName = FirebaseAuth.getInstance().getUid()+food_name_edt.getText().toString();
                try {
                    storageReference= FirebaseStorage.getInstance().getReference("FoodImage/"+fileName);

                }catch ( Exception e){
                    Toast.makeText(add_food_activity.this, "sumit"+""+e, Toast.LENGTH_SHORT).show();
                }
                storageReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                setInProgress(false);


//                        Toast.makeText(add_food_activity.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(add_food_activity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AndroidUtil.showToast(getApplicationContext()," failed to upload the image");
                            }
                        });


            }
        });


        food_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary,GALLARY_REQ_CODE);
            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            if(requestCode == GALLARY_REQ_CODE){
//                UserImage.setImageURI(data.getData());
                imageUri=data.getData();
                food_image_view.setTextColor(R.color.my_primary);
                food_image_view.setText("accepted");

//                textChosse.setVisibility(View.GONE);
            }

//            if(requestCode == CAMERA_REQ_CODE){
//                Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show();   //this is from camera
//                Bitmap img  = (Bitmap)(data.getExtras().get("data"));
//                imgcamera.setImageBitmap(img);
//            }
//            else{
//                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
//            }
        }

    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            donate.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            donate.setVisibility(View.VISIBLE);
        }
    }
}