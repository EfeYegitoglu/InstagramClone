package com.efeyegitoglu.instagram.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.efeyegitoglu.instagram.Home.HomeAdapter;
import com.efeyegitoglu.instagram.Home.HomeModel;
import com.efeyegitoglu.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GonderiFragment extends Fragment {

    View view;
    String postID;
    FirebaseAuth auth;

    List<HomeModel> mList;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    ImageView turnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_gonderi, container, false);

       tanimlamalar();
       gonderiList();

       return view;
    }

    private void tanimlamalar(){

        postID=getArguments().getString("other_userID");

        mList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.gonderi_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter=new HomeAdapter(mList,getContext());
        recyclerView.setAdapter(homeAdapter);

    }

    private void gonderiList(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getPostid().equals(postID)){
                        mList.add(homeModel);
                    }
                }
                homeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
