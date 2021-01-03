package com.efeyegitoglu.instagram.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.OtherProfileFragment;
import com.efeyegitoglu.instagram.Profile.ProfileFragment;
import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
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

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder> {

    List<YorumModel> modelList;
    Context context;

    FirebaseAuth auth;

    public YorumAdapter(List<YorumModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.yorum_layout,parent,false);
        return new YorumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        auth=FirebaseAuth.getInstance();

        final YorumModel yorumModel= modelList.get(position);

        kullaniciBilgiAl(holder.yorumProilePhoto,holder.yorumUserName,yorumModel.getGonderen());

        holder.yorumum.setText(yorumModel.getYorum());

        holder.yorumProilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yorumModel.getGonderen().equals(auth.getUid())) {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new ProfileFragment(),yorumModel.getGonderen());
                }else {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),yorumModel.getGonderen());
                }

            }
        });

        holder.yorumUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yorumModel.getGonderen().equals(auth.getUid())) {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new ProfileFragment(),yorumModel.getGonderen());
                }else {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),yorumModel.getGonderen());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView yorumProilePhoto;
        TextView yorumUserName,yorumum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            yorumum=itemView.findViewById(R.id.yorumum);
            yorumProilePhoto=itemView.findViewById(R.id.yorumProfilePhoto);
            yorumUserName=itemView.findViewById(R.id.yorumUserName);

        }
    }

    private void kullaniciBilgiAl(final CircleImageView photo, final TextView userName, String gonderenId){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ProfileImages").child(gonderenId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profileResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profileResmiModel.getResim().isEmpty()) {
                    Picasso.get().load(profileResmiModel.getResim()).into(photo);
                }
                userName.setText(profileResmiModel.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
