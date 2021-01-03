package com.efeyegitoglu.instagram.Profile;

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
import android.widget.LinearLayout;

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


public class TakipciFragment extends Fragment {

    View view;
    ImageView turn_profile;
    DatabaseReference reference;
    FirebaseAuth auth;

    List<String> mList;
    RecyclerView takipci_recycler;
    TakipciAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_takipci, container, false);

        tanimlamalar();
        TakipcileriAl();

        return view;
    }

    private void tanimlamalar() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        mList = new ArrayList<>();
        takipci_recycler = view.findViewById(R.id.takipci_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        takipci_recycler.setLayoutManager(layoutManager);
        adapter=new TakipciAdapter(mList,getContext(),getActivity());
        takipci_recycler.setAdapter(adapter);


        turn_profile = view.findViewById(R.id.turn_profile);
        turn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ProfileFragment());
            }
        });


    }

    private void TakipcileriAl() {

        reference.child("Takipçi").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mList.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                 mList.remove(dataSnapshot.getKey());
                 adapter.notifyDataSetChanged();
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
