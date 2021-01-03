package com.efeyegitoglu.instagram.Message;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.KullaniciKayitModel;
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

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.ViewHolder> {

    List<String> keyMessageList;
    Context context;
    Activity activity;
    DatabaseReference refProfileImage,refUserName;
    FirebaseAuth auth;

    public MessageShowAdapter(List<String> keyMessageList, Context context, Activity activity) {
        this.keyMessageList = keyMessageList;
        this.context = context;
        this.activity = activity;
        refProfileImage= FirebaseDatabase.getInstance().getReference();
        refUserName=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dm_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        refUserName.child("KullaniciKayitBilgi").child(keyMessageList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciKayitModel kayitModel=dataSnapshot.getValue(KullaniciKayitModel.class);

                holder.dm_userName.setText(kayitModel.getIsim());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refProfileImage.child("ProfileImages").child(keyMessageList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileResmiModel profilResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")){

                    Picasso.get().load(profilResmiModel.getResim()).into(holder.dm_Photo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.dm_page_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(context);
                changeFragment.changeWithParameterCanBack(new DmChatFragment(),keyMessageList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return keyMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView dm_Photo;
        TextView dm_userName;
        LinearLayout dm_page_LinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dm_page_LinearLayout=itemView.findViewById(R.id.dm_page_LinearLayout);
            dm_Photo=itemView.findViewById(R.id.dm_Photo);
            dm_userName=itemView.findViewById(R.id.dm_userName);
        }
    }
}
