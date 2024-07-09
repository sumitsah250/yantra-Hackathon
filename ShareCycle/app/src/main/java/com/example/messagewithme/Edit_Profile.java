package com.example.messagewithme;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
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

public class Edit_Profile extends AppCompatActivity {
    ImageView edt_profile_image,Edt_change_image;
    EditText edtUserName;
    StorageReference imageRef;
    Button Savebtn,logoutbtn;
    private final int GALLARY_REQ_CODE=1;
    Uri imageUri;
    StorageReference storageReference;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String phonenum="xxxxxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edt_profile_image= findViewById(R.id.edit_profile_pic);
        edtUserName=findViewById(R.id.edit_user_name);
        Savebtn=findViewById(R.id.btnsave);
        Edt_change_image=findViewById(R.id.edt_change_image);
        logoutbtn=findViewById(R.id.btn_logout);






        try{
            imageRef= FirebaseStorage.getInstance().getReference("images/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        }catch(Exception e){

        }
        if(imageRef !=null){
            downloadImage(imageRef);
        }

        //profile image

        //image picker
        Edt_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary,GALLARY_REQ_CODE);
            }
        });
        //image picker

        //saveing image
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyUser myUser=new MyUser();
                myUser=snapshot.getValue(MyUser.class);
                phonenum=myUser.getPhonenumbar();
                edtUserName.setText(myUser.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimpleDateFormat formater =new SimpleDateFormat("yyyy__MM__dd __HH __mm__ss", Locale.CANADA);
//                Date now = new Date();
//                String fileName = formater.format(now);
                //

                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();


                MyUser myUser = new MyUser(edtUserName.getText().toString(),phonenum );
                try {
                    firebaseDatabase.getReference().child("users").child(id).setValue(myUser);
                }catch (Exception e){
                    Toast.makeText(Edit_Profile.this, "nabin"+""+e, Toast.LENGTH_SHORT).show();
                }



                String fileName =id ; // getIntent().getExtras().getString("phone")+


                try {
                    storageReference= FirebaseStorage.getInstance().getReference("images/"+fileName);

                }catch ( Exception e){
                    Toast.makeText(Edit_Profile.this, ""+e, Toast.LENGTH_SHORT).show();
                }


                storageReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               edt_profile_image.setImageURI(null);
                                Toast.makeText(Edit_Profile.this, "profile picture updated successfully", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(Edit_Profile,MainActivity.class);
//                                intent.putExtra("id",id);
//                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AndroidUtil.showToast(getApplicationContext()," failed to upload the image");
                            }
                        });

            }
        });

        //saving image

        //logout
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                //// here
                AlertDialog.Builder delDialog = new AlertDialog.Builder(Edit_Profile.this);
                delDialog.setTitle("Are you sure ");
                delDialog.setMessage("Do you want to logout");
                delDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Edit_Profile.this, "logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Edit_Profile.this ,LoginPhoneNumberActivity.class));
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

                ///here
            }
        });
        //logout




    }
    private void downloadImage(StorageReference ref){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Glide.with(Edit_Profile.this).load(url).into(edt_profile_image);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            if(requestCode == GALLARY_REQ_CODE){
                edt_profile_image.setImageURI(data.getData());
                imageUri=data.getData();

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
}