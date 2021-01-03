package com.efeyegitoglu.instagram.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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


public class TakipFragment extends Fragment {

    View view;

    ImageView turn_profile,takip_options;
    DatabaseReference reference;
    FirebaseAuth auth;

    List<String> mList;
    TakipAdapter takipAdapter;
    RecyclerView takip_recycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_takip, container, false);

        tanimlamalar();
        TakipleriAl();


        return view;
    }

    void tanimlamalar() {


        reference = FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


        mList = new ArrayList<>();
        takip_recycler = view.findViewById(R.id.takip_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        takip_recycler.setLayoutManager(layoutManager);
        takipAdapter = new TakipAdapter(mList, getContext(), getActivity());
        takip_recycler.setAdapter(takipAdapter);

        turn_profile = view.findViewById(R.id.turn_profile);
        turn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new ProfileFragment());
            }
        });


    }



    private void TakipleriAl() {
        reference.child("Takip").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mList.add(dataSnapshot.getKey());
                takipAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mList.remove(dataSnapshot.getKey());
                takipAdapter.notifyDataSetChanged();
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
