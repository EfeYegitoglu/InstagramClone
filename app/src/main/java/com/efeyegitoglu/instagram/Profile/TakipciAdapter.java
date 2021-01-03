package com.efeyegitoglu.instagram.Profile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TakipciAdapter extends RecyclerView.Adapter<TakipciAdapter.ViewHolder> {

    List<String> mList;
    Context context;
    Activity activity;
    DatabaseReference reference;
    FirebaseAuth auth;

    public TakipciAdapter(List<String> mList, Context context, Activity activity) {
        this.mList = mList;
        this.context = context;
        this.activity = activity;
        reference= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.takipci_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        reference.child("ProfileImages").child(mList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profileResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profileResmiModel.getResim().equals("")) {
                    Picasso.get().load(profileResmiModel.getResim()).into(holder.takipci_photo);
                }

                holder.takipci_Name.setText(profileResmiModel.getIsim());
                holder.takipci_userName.setText(profileResmiModel.getUserName());


                holder.takipci_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeFragment changeFragment=new ChangeFragment(context);
                        changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),mList.get(position));
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
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView takipci_photo;
        TextView takipci_userName,takipci_Name;
        RelativeLayout takipci_list;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            takipci_photo=itemView.findViewById(R.id.takipci_photo);
            takipci_userName=itemView.findViewById(R.id.takipci_userName);
            takipci_Name=itemView.findViewById(R.id.takipci_Name);
            takipci_list=itemView.findViewById(R.id.takipci_list);


        }
    }
}
