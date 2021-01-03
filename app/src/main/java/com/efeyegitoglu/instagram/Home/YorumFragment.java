package com.efeyegitoglu.instagram.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.Profile.ProfileResmiModel;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class YorumFragment extends Fragment {

    View view;
    ImageView turnPost;
    CircleImageView yorum_photo;
    EditText yorumEdit;
    TextView paylasBtn;
    String postId;

    FirebaseAuth auth;

    RecyclerView yorum_Rec;
    YorumAdapter yorumAdapter;
    List<YorumModel> yorumModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_yorum, container, false);

        tanimlamalar();
        profilResmiAl();
        yorumlarıCek();

        return view;
    }

    private void tanimlamalar() {

        auth = FirebaseAuth.getInstance();
        postId = getArguments().getString("other_userID");

        turnPost = view.findViewById(R.id.turnPost);
        yorum_photo = view.findViewById(R.id.yorum_photo);
        yorumEdit = view.findViewById(R.id.yorumEdit);
        paylasBtn = view.findViewById(R.id.paylasBtn);
        paylasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yorumEdit.getText().equals("")) {
                    yorumYap();
                } else {

                }

            }
        });

        turnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new HomeFragment());
            }
        });


        yorumModelList = new ArrayList<>();
        yorum_Rec = view.findViewById(R.id.yorum_Rec);
        yorum_Rec.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        yorum_Rec.setLayoutManager(layoutManager);
        yorumAdapter = new YorumAdapter(yorumModelList, getContext());
        yorum_Rec.setAdapter(yorumAdapter);

    }

    private void yorumYap() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(postId);

        Map map = new HashMap();
        map.put("yorum", yorumEdit.getText().toString());
        map.put("gonderen", auth.getUid());
        reference.push().setValue(map);
        yorumEdit.setText("");

    }

    private void profilResmiAl() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileImages")
                .child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel model = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!model.getResim().isEmpty()) {
                    Picasso.get().load(model.getResim()).into(yorum_photo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void yorumlarıCek() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                yorumModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    YorumModel yorumModel = snapshot.getValue(YorumModel.class);
                    yorumModelList.add(yorumModel);
                }
                yorumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
