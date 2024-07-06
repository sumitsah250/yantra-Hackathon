package com.example.messagewithme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerAdapter_food extends RecyclerView.Adapter<RecyclerAdapter_food.VieHolder> {
    Context context;
    ArrayList<Food_details> foodDetails;
    String text;
    StorageReference imageRef;
    String url;


    public RecyclerAdapter_food(Context context, ArrayList<Food_details> foodDetails) {
        this.context = context;
        this.foodDetails = foodDetails;
    }



    @NonNull
    @Override
    public RecyclerAdapter_food.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_food, parent, false);
        return new VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_food.VieHolder holder, int position) {
        holder.expiretime.setVisibility(View.VISIBLE);
//        holder.imageView.setImageResource(foodDetails.get(position));
        if(foodDetails.get(position).food_expire.equals("")){
            holder.expiretime.setVisibility(View.GONE);
            holder.Category_text.setText("Others");
        }else{
            holder.Category_text.setText("food");
            holder.expiretime.setText("Expire At :"+foodDetails.get(position).food_expire);
        }
        holder.title.setText(foodDetails.get(position).food_name);

        String imgid = foodDetails.get(position).food_image_name;
        try {
            imageRef = FirebaseStorage.getInstance().getReference("FoodImage/" + imgid);
        } catch (Exception e) {

        }
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                if (imageRef != null) {
                    Glide.with(context).load(url).into(holder.imageView);
                }

                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if (imageRef != null) {
            Glide.with(context).load(url).into(holder.imageView);
        }else{
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,food_informatino_activity.class);
                intent.putExtra("food_name",foodDetails.get(position).food_name);
                intent.putExtra("food_image_name",foodDetails.get(position).food_image_name);
                intent.putExtra("food_expire",foodDetails.get(position).food_expire);
                intent.putExtra("food_location",foodDetails.get(position).food_location);
                intent.putExtra("quantity",foodDetails.get(position).food_quantity);
                intent.putExtra("Contact_number",foodDetails.get(position).Phon_Number);
                context.startActivity(intent);

            }
        });
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder delDialog = new AlertDialog.Builder(context);
                delDialog.setTitle("Are you sure ");
                delDialog.setMessage("Do you want to remove this item ?");
                delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String foodid = FirebaseAuth.getInstance().getCurrentUser().getUid()+foodDetails.get(position).food_name;
                        FirebaseDatabase.getInstance().getReference().child("foodDetails")
                                .child(foodid)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        foodDetails.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(context, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                                );


                    }
                });
                delDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                delDialog.show();



                return false;

            }
        });

    }

    @Override
    public int getItemCount() {
        return foodDetails.size();
    }

    public class VieHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView expiretime, title,Category_text;
        ConstraintLayout constraintLayout;



        public VieHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.row_image);
            expiretime = itemView.findViewById(R.id.expire_time);
            title = itemView.findViewById(R.id.row_title);
            Category_text=itemView.findViewById(R.id.category_text);
            constraintLayout=itemView.findViewById(R.id.mainRowRecycler);
        }
    }

    private void downloadImage(StorageReference ref) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();

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
