package com.efeyegitoglu.instagram.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Home.HomeModel;
import com.efeyegitoglu.instagram.Profile.PostsAdapter;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    View view;
    TextView searchText;
    FirebaseAuth auth;

    List<HomeModel> mList;
    RecyclerView recyclerView;
    PostsAdapter postsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        tanimlamalar();
        kesfetList();

        return view;
    }

    void tanimlamalar(){
        auth=FirebaseAuth.getInstance();

        searchText=view.findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.changeCanBack(new FindUserFragment());
            }
        });

        mList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.kesfet_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        postsAdapter=new PostsAdapter(mList,getContext());
        recyclerView.setAdapter(postsAdapter);

    }

    private void kesfetList(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getPostid().contains("g") && !homeModel.getId().equals(auth.getUid())){
                        mList.add(homeModel);
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
