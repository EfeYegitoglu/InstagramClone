package com.efeyegitoglu.instagram.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Activity.AddStoryActivity;
import com.efeyegitoglu.instagram.Activity.StoryActivity;
import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context context;
    private List<StoryModel> mStory;
    private FirebaseAuth auth;

    public StoryAdapter(Context context, List<StoryModel> mStory) {
        this.context = context;
        this.mStory = mStory;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_story_item, parent, false);
            return new StoryAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.story, parent, false);
            return new StoryAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final StoryModel storyModel = mStory.get(position);



        userInfo(holder, storyModel.getUserId(), position);

        if (holder.getAdapterPosition() != 0 ) {
            seenStory(holder, storyModel.getUserId());

        }

        if (holder.getAdapterPosition() == 0) {
            myStory(holder.addStoryPlus, false);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0) {
                    myStory(holder.addStoryPlus, true);
                } else {
                    Intent intent = new Intent(context, StoryActivity.class);
                    intent.putExtra("userId", storyModel.getUserId());
                    context.startActivity(intent);
                }
            }
        });
        System.out.println(mStory.size());
    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView storyPhoto, storyPhotoSeen, addStoryPlus;
        TextView storyUserName;
        LinearLayout hor_linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hor_linear=itemView.findViewById(R.id.hor_linear);
            storyPhoto = itemView.findViewById(R.id.storyPhoto);
            storyPhotoSeen = itemView.findViewById(R.id.storyPhotoSeen);
            addStoryPlus = itemView.findViewById(R.id.addStoryPlus);
            storyUserName = itemView.findViewById(R.id.storyUserName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void userInfo(final ViewHolder viewHolder, String userId, final int pos) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileImages").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileResmiModel profileResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profileResmiModel.getResim().equals("")) {
                    Picasso.get().load(profileResmiModel.getResim()).into(viewHolder.storyPhoto);
                }


                 if (pos !=0){
                    if (!profileResmiModel.getResim().equals("")) {
                        Picasso.get().load(profileResmiModel.getResim()).into(viewHolder.storyPhotoSeen);
                    }
                    viewHolder.storyUserName.setText(profileResmiModel.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myStory(final ImageView imageView, final boolean click) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(auth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                long timeCurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StoryModel storyModel = snapshot.getValue(StoryModel.class);
                    if (timeCurrent > storyModel.getTimeStart() && timeCurrent < storyModel.getTimeEnd()) {
                        count++;
                    }
                }

                if (click) {
                    if (count > 0) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hikayeni Gör", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, StoryActivity.class);
                                intent.putExtra("userId", auth.getUid());
                                context.startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Hikaye Ekle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, AddStoryActivity.class);
                                context.startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        Intent intent = new Intent(context, AddStoryActivity.class);
                        context.startActivity(intent);

                    }
                } else {
                    if (count > 0) {
                        imageView.setVisibility(View.GONE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void seenStory(final ViewHolder viewHolder, String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.child("views").child(auth.getUid()).exists() &&
                            System.currentTimeMillis() < snapshot.getValue(StoryModel.class).getTimeEnd()) {
                        i++;
                    }
                }

                if (i > 0) {
                    viewHolder.storyPhoto.setVisibility(View.VISIBLE);
                    viewHolder.storyPhotoSeen.setVisibility(View.GONE);
                } else {
                    viewHolder.storyPhoto.setVisibility(View.GONE);
                    viewHolder.storyPhotoSeen.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
