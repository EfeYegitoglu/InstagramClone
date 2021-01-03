package com.efeyegitoglu.instagram.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class PostsFragment extends Fragment {

    View view;
    FirebaseAuth auth;

    //Posts recycler
    RecyclerView recyclerView;
    PostsAdapter postsAdapter;
    List<HomeModel> modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view=inflater.inflate(R.layout.fragment_posts, container, false);

       tanimlamalar();
       postGetirmeListesi();

       return view;
    }

    private void tanimlamalar(){
        auth=FirebaseAuth.getInstance();

        //Recycler

        modelList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.profil_post_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        postsAdapter=new PostsAdapter(modelList,getContext());
        recyclerView.setAdapter(postsAdapter);
    }

    private void postGetirmeListesi(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getId().equals(auth.getUid())){
                        modelList.add(homeModel);
                    }
                }
                postsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
