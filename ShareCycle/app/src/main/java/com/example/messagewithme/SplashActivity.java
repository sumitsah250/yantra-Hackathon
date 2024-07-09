package com.example.messagewithme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messagewithme.Utils.MyUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //just for test
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                MyUser myUser= new MyUser();
//                myUser=snapshot.getValue(MyUser.class);
//                Toast.makeText(SplashActivity.this, ""+myUser.getPhonenumbar()+myUser.getUsername(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        // Toast.makeText(SplashActivity.this, ""+FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
     // just for test
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              if(FirebaseAuth.getInstance().getCurrentUser()!= null){
                  startActivity(new Intent(SplashActivity.this,MainActivity.class));

                  finish();
              }else {
                  startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));

                  finish();
              }

            }
        },2500);
    }
}