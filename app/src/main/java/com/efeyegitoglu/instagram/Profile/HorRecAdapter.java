package com.efeyegitoglu.instagram.Profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class HorRecAdapter extends RecyclerView.Adapter<HorRecAdapter.ViewHolder> {

    List<String> mList;
    Context context;
    Activity activity;
    DatabaseReference reference;
    FirebaseAuth auth;
    String Kontrol="";


    public HorRecAdapter(List<String> mList, Context context, Activity activity) {
        this.mList = mList;
        this.context = context;
        this.activity = activity;
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_recycler_other_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        reference.child("ProfileImages").child(mList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profileResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!holder.rec_btn.getText().equals("Takip")){
                    if (!profileResmiModel.getResim().equals("")) {
                        Picasso.get().load(profileResmiModel.getResim()).into(holder.rec_photo);
                    }

                    holder.rec_userName.setText(profileResmiModel.getUserName());
                    holder.rec_Name.setText(profileResmiModel.getIsim());
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference.child("Istekler").child(mList.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(auth.getUid())) {
                    Kontrol="istek";
                    holder.rec_btn.setText("İstek Göderildi");
                    holder.rec_btn.setBackgroundResource(R.drawable.button_profile_background);
                    holder.rec_btn.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.rec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Kontrol.equals("")) {
                    reference.child("Istekler").child(mList.get(position)).child(auth.getUid()).child("istek").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Kontrol="istek";
                                holder.rec_btn.setText("İstek Göderildi");
                                holder.rec_btn.setBackgroundResource(R.drawable.button_profile_background);
                                holder.rec_btn.setTextColor(Color.BLACK);
                                Toast.makeText(context,"İstek Göderildi",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(context,"Hata",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                } else if (Kontrol.equals("istek")) {
                    reference.child("Istekler").child(mList.get(position)).child(auth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Kontrol="";
                                holder.rec_btn.setText("Takip Et");
                                holder.rec_btn.setBackgroundResource(R.drawable.button_takip_et);
                                holder.rec_btn.setTextColor(Color.WHITE);
                                Toast.makeText(context,"İstek İptal Edildi",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(context,"Hata",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });










    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView rec_photo;
        TextView rec_userName, rec_Name;
        Button rec_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rec_btn = itemView.findViewById(R.id.rec_btn);
            rec_photo = itemView.findViewById(R.id.rec_photo);
            rec_userName = itemView.findViewById(R.id.rec_userName);
            rec_Name = itemView.findViewById(R.id.rec_Name);
        }


    }
}
