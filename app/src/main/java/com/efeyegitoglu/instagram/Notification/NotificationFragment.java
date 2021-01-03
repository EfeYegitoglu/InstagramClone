package com.efeyegitoglu.instagram.Notification;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Search.AraModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {
    View view;
    String userID;

    DatabaseReference reference;
    FirebaseAuth auth;

    List<String> keyList;
    RecyclerView confirm_recycler;
    ConfirmAdapter confirmAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        tanimlamalar();
        istekleriAl();

        return view;
    }

    void tanimlamalar() {


        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        keyList = new ArrayList<>();
        confirm_recycler = view.findViewById(R.id.confirm_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        confirm_recycler.setLayoutManager(layoutManager);
        confirmAdapter=new ConfirmAdapter(keyList,getContext(),getActivity());
        confirm_recycler.setAdapter(confirmAdapter);


    }

    private void istekleriAl() {
        reference.child("Istekler").child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                keyList.add(dataSnapshot.getKey());
                confirmAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                keyList.remove(dataSnapshot.getKey());
                confirmAdapter.notifyDataSetChanged();
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
