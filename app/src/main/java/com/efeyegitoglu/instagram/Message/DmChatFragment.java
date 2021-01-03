package com.efeyegitoglu.instagram.Message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.efeyegitoglu.instagram.Profile.KullaniciKayitModel;
import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class DmChatFragment extends Fragment {

    View view;
    String otherID;

    ImageView turnBackToDM;
    CircleImageView message_userPhoto ;
    TextView message_userName,message_Name,send_dm_Button;
    EditText send_message_editText;
    DatabaseReference referenceMessage;


    DatabaseReference referenceNames,referencePhoto;
    FirebaseAuth auth;

    List<SendMessageModel> list;
    RecyclerView Message_recyclerView;
    SendMessageAdapter messageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dm_chat_layout, container, false);

        tanimlamalar();
        getNames();
        getPhoto();
        loadMessages();

        return view;
    }

    void tanimlamalar(){
        otherID=getArguments().getString("other_userID");

        referenceMessage=FirebaseDatabase.getInstance().getReference();
        referenceNames= FirebaseDatabase.getInstance().getReference();
        referencePhoto=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();

        message_userPhoto=view.findViewById(R.id.message_userPhoto);
        message_userName=view.findViewById(R.id.message_userName);
        message_Name=view.findViewById(R.id.message_Name);
        send_message_editText=view.findViewById(R.id.send_message_editText);

        turnBackToDM=view.findViewById(R.id.turnBackToDM);
        turnBackToDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.changeCanBack(new MessageFragment());

            }
        });

        send_dm_Button=view.findViewById(R.id.send_dm_Button);
        send_dm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=send_message_editText.getText().toString();
                send_message_editText.setText("");
                if (!message.equals("")) {
                    sendMessage(message);
                }


            }
        });

        list=new ArrayList<SendMessageModel>();
        Message_recyclerView=view.findViewById(R.id.Message_recyclerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        Message_recyclerView.setLayoutManager(layoutManager);
        messageAdapter=new SendMessageAdapter(getContext(),list,getActivity());
        Message_recyclerView.setAdapter(messageAdapter);


    }

    void getNames(){
        referenceNames.child("KullaniciKayitBilgi").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciKayitModel kayitModel=dataSnapshot.getValue(KullaniciKayitModel.class);

                message_userName.setText(kayitModel.getUserName());
                message_Name.setText(kayitModel.getIsim());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void getPhoto(){

        referencePhoto.child("ProfileImages").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileResmiModel profilResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")) {
                    Picasso.get().load(profilResmiModel.getResim()).into(message_userPhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void sendMessage(String text){
        final String key=referenceMessage.child("Mesajlar").child(auth.getUid()).child(otherID).push().getKey();

        final Map messageMap=new HashMap();
        messageMap.put("text",text);
        messageMap.put("from",auth.getUid());

        referenceMessage.child("Mesajlar").child(auth.getUid()).child(otherID).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    referenceMessage.child("Mesajlar").child(otherID).child(auth.getUid()).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

            }
        });
    }

    void loadMessages(){
        referenceMessage.child("Mesajlar").child(auth.getUid()).child(otherID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SendMessageModel messageModel=dataSnapshot.getValue(SendMessageModel.class);

                list.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                Message_recyclerView.scrollToPosition(list.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
