package com.efeyegitoglu.instagram.Message;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendMessageAdapter extends  RecyclerView.Adapter<SendMessageAdapter.ViewHolder> {


    Context context;
    List<SendMessageModel> list;
    Boolean state=false;
    Activity activity;
    int view_send=1,view_received=2;
    FirebaseAuth auth;
    DatabaseReference refProfileImage;



    public SendMessageAdapter(Context context, List<SendMessageModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        state=false;
        this.activity = activity;
        auth=FirebaseAuth.getInstance();
        refProfileImage=FirebaseDatabase.getInstance().getReference();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType==view_send){
            view= LayoutInflater.from(context).inflate(R.layout.send_dm_layout,parent,false);
            return new ViewHolder(view);
        }else
        {
            view= LayoutInflater.from(context).inflate(R.layout.received_dm_layout,parent,false);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getText().toString());

        if (!list.get(position).getFrom().equals(auth.getUid())){
            refProfileImage.child("ProfileImages").child(list.get(position).getFrom()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ProfileResmiModel profilResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

                    if (!profilResmiModel.getResim().equals("")){
                        Picasso.get().load(profilResmiModel.getResim()).into(holder.dm_otherUserPhoto);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CircleImageView dm_otherUserPhoto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (state==true){
                textView=itemView.findViewById(R.id.sendText);

            }else {
                textView=itemView.findViewById(R.id.receivedText);
                dm_otherUserPhoto=itemView.findViewById(R.id.dm_otherUserPhoto);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getFrom().equals(auth.getUid())){
            state=true;
            return view_send;
        }
        else {
            state=false;
            return view_received;
        }
    }
}
