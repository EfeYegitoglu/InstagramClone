package com.efeyegitoglu.instagram.Notification;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.ViewHolder> {

    List<String> keyList;
    Context context;
    Activity activity;
    DatabaseReference reference;
    FirebaseAuth auth;

    public ConfirmAdapter(List<String> keyList, Context context, Activity activity) {
        this.keyList = keyList;
        this.context = context;
        this.activity = activity;
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follow_check_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        reference.child("ProfileImages").child(keyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profilResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")) {
                    Picasso.get().load(profilResmiModel.getResim()).into(holder.confirm_profile_photo);
                }

                holder.confirm_userName.setText(profilResmiModel.getUserName());

                holder.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Confirm(auth.getUid(), keyList.get(position));

                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Delete(auth.getUid(), keyList.get(position));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView confirm_profile_photo;
        TextView confirm_userName;
        Button confirm, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            confirm_profile_photo = itemView.findViewById(R.id.confirm_profile_photo);
            confirm_userName = itemView.findViewById(R.id.confirm_userName);
            confirm = itemView.findViewById(R.id.confirm);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    void Confirm(final String userID, final String otherID) {

        reference.child("Istekler").child(userID).child(otherID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    reference.child("Takip").child(otherID).child(userID).child("takip").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                reference.child("Takipçi").child(userID).child(otherID).child("takipçi").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Kabul Edildi", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        }
                    });


                } else {
                    Toast.makeText(context, "Hata", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    void Delete(final String userID, final String otherID) {

        reference.child("Istekler").child(userID).child(otherID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Reddedildi", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Hata", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


}
