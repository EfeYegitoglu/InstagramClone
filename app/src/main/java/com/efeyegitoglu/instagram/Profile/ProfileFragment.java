package com.efeyegitoglu.instagram.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.efeyegitoglu.instagram.Activity.LoginActivity;
import com.efeyegitoglu.instagram.Home.HomeModel;
import com.efeyegitoglu.instagram.Notification.NotificationFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Share.ShareFragment;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    View view;
    Button profili_düzenle_button;
    TextView profile_userName, NameText, bioText, gonderiSayiText, takipçiSayiText, takipSayiText, websiteText;
    CircleImageView profile_photo;
    ImageView profile_options_menu;
    LinearLayout click_follow;
    LinearLayout click_follower;

    DatabaseReference referenceKayit, referenceProfile, referenceProfilResmi, reference;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        profilAdapterYanaKaydirma();
        tanimlamalar();
        kullaniciKayitBilgiGetir();
        profilBilgiGetir();
        profilResmiGetir();
        TakipSayisi();
        TakipçiSayi();
        gonderiSayisiGetir();


        return view;
    }

    void profilAdapterYanaKaydirma() {

        ViewPager viewPager = view.findViewById(R.id.ViewPager);
        PagerAdapter adapter = new PageSlideAdapter(getChildFragmentManager(), 0);
        ((PageSlideAdapter) adapter).addFragment(new PostsFragment());
        ((PageSlideAdapter) adapter).addFragment(new EtiketFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab1);
        tabLayout.getTabAt(1).setIcon(R.drawable.profile_tab2);

    }

    void tanimlamalar() {
        auth = FirebaseAuth.getInstance();
        referenceKayit = FirebaseDatabase.getInstance().getReference().child("KullaniciKayitBilgi").child(auth.getUid());
        referenceProfile = FirebaseDatabase.getInstance().getReference();
        referenceProfilResmi = FirebaseDatabase.getInstance().getReference().child("ProfileImages").child(auth.getUid());
        reference = FirebaseDatabase.getInstance().getReference();


        profile_userName = view.findViewById(R.id.profile_userName);
        NameText = view.findViewById(R.id.NameText);
        profile_photo = view.findViewById(R.id.profile_photo);
        bioText = view.findViewById(R.id.bioText);
        gonderiSayiText = view.findViewById(R.id.gonderiSayiText);
        takipçiSayiText = view.findViewById(R.id.takipçiSayiText);
        takipSayiText = view.findViewById(R.id.takipSayiText);
        websiteText = view.findViewById(R.id.websiteText);
        profili_düzenle_button = view.findViewById(R.id.profili_düzenle_button);
        profili_düzenle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.changeCanBack(new EditProfileFragment());
            }
        });

        profile_options_menu = view.findViewById(R.id.profile_options_menu);
        profile_options_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        click_follow = view.findViewById(R.id.click_follow);
        click_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.changeCanBack(new TakipFragment());
            }
        });

        click_follower = view.findViewById(R.id.click_follower);
        click_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.changeCanBack(new TakipciFragment());
            }
        });


    }

    void kullaniciKayitBilgiGetir() {

        referenceKayit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciKayitModel kayitModel = dataSnapshot.getValue(KullaniciKayitModel.class);

                NameText.setText(kayitModel.getIsim());
                profile_userName.setText(kayitModel.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void profilBilgiGetir() {

        referenceProfile.child("ProfilBilgi").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfilBilgiModel pbm = dataSnapshot.getValue(ProfilBilgiModel.class);


                Log.i("okuu", dataSnapshot.getValue().toString());
                //NameText.setText(pbm.getIsim());
                bioText.setText(pbm.getBio());
                websiteText.setText(pbm.getWebsite());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void profilResmiGetir() {

        referenceProfilResmi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profilResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")) {

                    //picasso burda
                    Picasso.get().load(profilResmiModel.getResim()).into(profile_photo);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void TakipSayisi() {

        final List<String> takipList = new ArrayList<>();

        reference.child("Takip").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                takipList.add(dataSnapshot.getKey());
                takipSayiText.setText(String.valueOf(takipList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                takipList.remove(dataSnapshot.getKey());
                takipSayiText.setText(String.valueOf(takipList.size()));

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void TakipçiSayi() {

        final List<String> takipçiList = new ArrayList<>();

        reference.child("Takipçi").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                takipçiList.add(dataSnapshot.getKey());
                takipçiSayiText.setText(String.valueOf(takipçiList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                takipçiList.remove(dataSnapshot.getKey());
                takipçiSayiText.setText(String.valueOf(takipçiList.size()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderiSayisiGetir() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getId().equals(auth.getUid())){
                        i++;
                    }
                }
                gonderiSayiText.setText(String.valueOf(i));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
