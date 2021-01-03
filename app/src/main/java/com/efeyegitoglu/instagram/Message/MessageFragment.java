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
import android.widget.ImageView;
import android.widget.TextView;

import com.efeyegitoglu.instagram.Home.HomeFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    View view;
    ImageView turnBackToHome;
    TextView userName_dm;

    DatabaseReference refMessage;
    FirebaseAuth auth;

    List<String> keyMessageList;
    RecyclerView DM_menu_recyclerView;
    MessageShowAdapter showAdapter;

    String otherID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_message, container, false);

        tanimlamalar();
        getMessages();

        return view;
    }

    void tanimlamalar(){



        refMessage= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


        userName_dm=view.findViewById(R.id.message_userName);

        turnBackToHome=view.findViewById(R.id.turnBackToHome);
        turnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new HomeFragment());
            }
        });

        keyMessageList=new ArrayList<>();
        DM_menu_recyclerView=view.findViewById(R.id.DM_menu_recyclerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        DM_menu_recyclerView.setLayoutManager(layoutManager);
        showAdapter=new MessageShowAdapter(keyMessageList,getContext(),getActivity());
        DM_menu_recyclerView.setAdapter(showAdapter);


    }

    void getMessages(){
        refMessage.child("Mesajlar").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                keyMessageList.add(dataSnapshot.getKey());
                showAdapter.notifyDataSetChanged();

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
