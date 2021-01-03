package com.efeyegitoglu.instagram.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.Home.StoryModel;
import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    int count = 0;
    long pressTime = 0L;
    long limit = 500L;

    StoriesProgressView storiesProgressView;
    ImageView image;
    CircleImageView storyPhoto;
    TextView storyUserName;

    List<String> images;
    List<String> storyIds;
    String userId;

    LinearLayout seenStory;
    TextView seenNumber;
    ImageView storyDelete;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;

            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        storyPhoto = findViewById(R.id.storyPhoto);
        storyUserName = findViewById(R.id.storyUserName);

        storyDelete=findViewById(R.id.storyDelete);
        seenNumber=findViewById(R.id.seenNumber);
        seenStory=findViewById(R.id.seenStory);

        seenStory.setVisibility(View.GONE);
        storyDelete.setVisibility(View.GONE);




        userId = getIntent().getStringExtra("userId");

        if (userId.equals(FirebaseAuth.getInstance().getUid())) {
            seenStory.setVisibility(View.VISIBLE);
            storyDelete.setVisibility(View.VISIBLE);
        }

        getStories(userId);
        userInfo(userId);

        View reverse = findViewById(R.id.reverseView);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skipView);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);


        storyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story").child(userId).child(storyIds.get(count));
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(StoryActivity.this,"Hikaye Silindi",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void addView(String storyId){
        FirebaseDatabase.getInstance().getReference("Story").child(userId).child(storyId)
                .child("views").child(FirebaseAuth.getInstance().getUid()).setValue(true);
    }

    private void seenNumber(String storyId){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story").child(userId)
                .child(storyId).child("views");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                seenNumber.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNext() {
        Picasso.get().load(images.get(++count)).into(image);

        addView(storyIds.get(count));
        seenNumber(storyIds.get(count));
    }

    @Override
    public void onPrev() {
        if ((count - 1) < 0) return;
        Picasso.get().load(images.get(--count)).into(image);
        seenNumber(storyIds.get(count));
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }



    private void getStories(String userId) {

        images = new ArrayList<>();
        storyIds = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                storyIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StoryModel storyModel = snapshot.getValue(StoryModel.class);
                    long timeCurrent = System.currentTimeMillis();
                    if (timeCurrent > storyModel.getTimeStart() && timeCurrent < storyModel.getTimeEnd()) {
                        images.add(storyModel.getImageUrl());
                        System.out.println(storyModel.getImageUrl());
                        storyIds.add(storyModel.getStoryId());
                        System.out.println(storyModel.getStoryId());
                    }
                }
                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(StoryActivity.this);
                storiesProgressView.startStories(count);

                Picasso.get().load(images.get(count)).into(image);

                addView(storyIds.get(count));
                seenNumber(storyIds.get(count));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userInfo(String userId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileImages").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileResmiModel profileResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);
                if (!profileResmiModel.getResim().equals("")) {
                    Picasso.get().load(profileResmiModel.getResim()).into(storyPhoto);
                }
                storyUserName.setText(profileResmiModel.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
