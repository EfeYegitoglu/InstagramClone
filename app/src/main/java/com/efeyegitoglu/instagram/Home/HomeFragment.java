package com.efeyegitoglu.instagram.Home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.efeyegitoglu.instagram.Message.MessageFragment;
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


public class HomeFragment extends Fragment {
    View view;
    ImageView go_DM;

    FirebaseAuth auth;

    //Post Recycler
    List<String> takipList;
    List<HomeModel> postIdList;
    RecyclerView home_Rec;
    HomeAdapter homeAdapter;

    //Story Recycler
    List<StoryModel> storyModelList;
    RecyclerView storyRecycler;
    StoryAdapter storyAdapter;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        tanimlamalar();
        readStory();



        return view;
    }

    void tanimlamalar(){
        go_DM=view.findViewById(R.id.go_DM);
        go_DM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.changeCanBack(new MessageFragment());
            }
        });


        auth=FirebaseAuth.getInstance();

        TakipListeEkle();

        //Post
        postIdList=new ArrayList<>();
        home_Rec=view.findViewById(R.id.home_Rec);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        home_Rec.setLayoutManager(layoutManager);
        homeAdapter=new HomeAdapter(postIdList,getContext());
        home_Rec.setAdapter(homeAdapter);

        //Story
        storyModelList=new ArrayList<>();
        storyRecycler=view.findViewById(R.id.recylerStory);
        storyRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRecycler.setLayoutManager(layoutManager1);
        storyAdapter=new StoryAdapter(getContext(),storyModelList);
        storyRecycler.setAdapter(storyAdapter);

    }

    private void readStory(){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timeCurrent=System.currentTimeMillis();
                storyModelList.clear();
                storyModelList.add(new StoryModel("",0,0,"",auth.getUid()));
                for (String id: takipList){
                    int countStory=0;
                    StoryModel storyModel=null;
                    for (DataSnapshot snapshot:dataSnapshot.child(id).getChildren()){
                        storyModel=snapshot.getValue(StoryModel.class);
                        if (timeCurrent > storyModel.getTimeStart() && timeCurrent < storyModel.getTimeEnd()) {
                            countStory++;
                        }
                    }
                    if (countStory>0){
                        storyModelList.add(storyModel);

                    }
                }
                storyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void TakipListeEkle(){
        takipList=new ArrayList<>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Takip").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                takipList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    takipList.add(snapshot.getKey());
                    takipList.add(auth.getUid());
                }
                PostOku();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void PostOku(){

          DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");

          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  postIdList.clear();
                  for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                      HomeModel homeModel=snapshot.getValue(HomeModel.class);
                      for (String id:takipList){
                          if (homeModel.getId().equals(id)){
                              postIdList.add(homeModel);
                          }
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
