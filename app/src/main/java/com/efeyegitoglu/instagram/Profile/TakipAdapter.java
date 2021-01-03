package com.efeyegitoglu.instagram.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class TakipAdapter extends RecyclerView.Adapter<TakipAdapter.ViewHolder> {

    List<String> mList;
    Context context;
    Activity activity;
    DatabaseReference reference;
    FirebaseAuth auth;

    public TakipAdapter(List<String> mList, Context context, Activity activity) {
        this.mList = mList;
        this.context = context;
        this.activity = activity;
        reference=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.takip_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       reference.child("ProfileImages").child(mList.get(position)).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               ProfileResmiModel profileResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

               if (!profileResmiModel.getResim().equals("")){
                   Picasso.get().load(profileResmiModel.getResim()).into(holder.takip_photo);
               }

               holder.takip_userName.setText(profileResmiModel.getUserName());
               holder.takip_user_Name.setText(profileResmiModel.getIsim());

               holder.takip_list.setOnClickListener(new View.OnClickListener() {
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


        holder.takip_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOptions();
            }
        });

        holder.takip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTakip();

            }
        });




    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView takip_photo,alert_takip_photo;
        TextView takip_userName,takip_user_Name;
        Button takip_btn;
        ImageView takip_options;
        RelativeLayout takip_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            takip_photo=itemView.findViewById(R.id.takip_photo);
            takip_user_Name=itemView.findViewById(R.id.takip_user_Name);
            takip_userName=itemView.findViewById(R.id.takip_userName);
            takip_btn=itemView.findViewById(R.id.takip_btn);
            takip_options=itemView.findViewById(R.id.takip_options);
            takip_list=itemView.findViewById(R.id.takip_list);




        }
    }

    private void showDialogOptions() {


        //Dialog gösterme işlemleri
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.alert_takip_options_layout, null);

        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(view).create();
        alertDialog.show();

    }

    private void DialogTakip(){

        //Dialog gösterme işlemleri
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_takip_layout, null);



        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(view).create();
        alertDialog.show();

    }
}
