package com.efeyegitoglu.instagram.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.Home.HomeAdapter;
import com.efeyegitoglu.instagram.Home.HomeModel;
import com.efeyegitoglu.instagram.Home.ViewPageAdapter;
import com.efeyegitoglu.instagram.Message.DmChatFragment;
import com.efeyegitoglu.instagram.Notification.NotificationFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Search.FindUserFragment;
import com.efeyegitoglu.instagram.Share.ShareFragment;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import static com.efeyegitoglu.instagram.R.layout.bottom_sheet__takip_layout;


public class OtherProfileFragment extends Fragment {

    View view;
    String otherID, Kontrol = "";

    ImageView other_turnBack_icon, profile_options_menu,btn_openRec;
    TextView other_gonderiSayiText,other_profile_userName, other_NameText, other_bioText, other_websiteText, other_takipSayiText, other_takipçiSayiText;
    CircleImageView other_profile_photo;
    Button other_user_MesajAt, takipEt_Button;

    DatabaseReference referenceName, referenceProfilResmi, referenceProfilBilgi, reference;

    FirebaseAuth auth;
    DatabaseReference refTakip, refIstek, refTakipçi;

    BottomSheetDialog bottomSheetDialog;


    //Horizontal Recycler
    RecyclerView recycler_horizontal;
    List<String> horList;
    HorRecAdapter horRecAdapter;
    LinearLayout hor_Lin;
    String btnControl="closed";

    //Post Recycler
    RecyclerView recyclerView_post;
    List<HomeModel> homeModelList;
    PostsAdapter homeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        tanimlamalar();
        //otherProfileTanLayout();
        otherNameGetir();
        otherProfilResmiGetir();
        otherPtofilBilgiGetir();

        KontrolIstek();
        TakipKontrol();
        TakipSayi();
        TakipçiSayi();
        gonderiSayisiGetir();

        HorRecAdd();

        return view;
    }

    /*private void otherProfileTanLayout() {

        ViewPager viewPager = view.findViewById(R.id.other_ViewPager);
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager(), 0);
        adapter.addFragment(new NotificationFragment());
        adapter.addFragment(new ShareFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.other_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.profile_tab1);
        tabLayout.getTabAt(1).setIcon(R.drawable.profile_tab2);

    }*/


    private void tanimlamalar() {
        Sheet();


        otherID = getArguments().getString("other_userID");


        referenceName = FirebaseDatabase.getInstance().getReference();
        referenceProfilResmi = FirebaseDatabase.getInstance().getReference();
        referenceProfilBilgi = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        refTakip = FirebaseDatabase.getInstance().getReference();
        refIstek = FirebaseDatabase.getInstance().getReference();
        refTakipçi = FirebaseDatabase.getInstance().getReference();


        other_takipçiSayiText = view.findViewById(R.id.other_takipçiSayiText);
        other_profile_userName = view.findViewById(R.id.other_profile_userName);
        other_NameText = view.findViewById(R.id.other_NameText);
        other_bioText = view.findViewById(R.id.other_bioText);
        other_websiteText = view.findViewById(R.id.other_websiteText);
        other_profile_photo = view.findViewById(R.id.other_profile_photo);
        other_takipSayiText = view.findViewById(R.id.other_takipSayiText);
        other_gonderiSayiText=view.findViewById(R.id.other_gonderiSayiText);

        other_turnBack_icon = view.findViewById(R.id.other_turnBack_icon);
        other_turnBack_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new FindUserFragment());
            }
        });

        profile_options_menu = view.findViewById(R.id.profile_options_menu);
        profile_options_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        other_user_MesajAt = view.findViewById(R.id.other_user_MesajAt);
        other_user_MesajAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.changeWithParameterCanBack(new DmChatFragment(), otherID);
            }
        });


        takipEt_Button = view.findViewById(R.id.takipEt_Button);
        takipEt_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Kontrol.equals("")) {

                    IstekAt(auth.getUid(), otherID);

                } else if (Kontrol.equals("istek")) {

                    IstekGeriCek(auth.getUid(), otherID);

                } else {

                    bottomSheetDialog.show();
                }


            }
        });


        horRec();



        btn_openRec=view.findViewById(R.id.btn_openRec);
        btn_openRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnControl.equals("closed")) {
                    hor_Lin.setVisibility(View.VISIBLE);
                    btnControl="open";
                }else {
                    hor_Lin.setVisibility(View.GONE);
                    btnControl="closed";
                }


            }
        });

        postRec();
        postList();

    }

    private void postRec(){

        homeModelList=new ArrayList<>();
        recyclerView_post=view.findViewById(R.id.other_Post_recycler);
        recyclerView_post.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new GridLayoutManager(getContext(),3);
        recyclerView_post.setLayoutManager(layoutManager);
        homeAdapter=new PostsAdapter(homeModelList,getContext());
        recyclerView_post.setAdapter(homeAdapter);
    }

    private void postList(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               homeModelList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getId().equals(otherID)){
                        homeModelList.add(homeModel);
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void horRec() {
        hor_Lin = view.findViewById(R.id.hor_Lin);
        horList = new ArrayList<>();
        recycler_horizontal = view.findViewById(R.id.recycler_horizontal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_horizontal.setLayoutManager(layoutManager);
        horRecAdapter = new HorRecAdapter(horList, getContext(), getActivity());
        recycler_horizontal.setAdapter(horRecAdapter);
    }

    private void Sheet() {
        View view = LayoutInflater.from(getContext()).inflate(bottom_sheet__takip_layout, null);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);
    }


    private void showDialog() {


        //Dialog gösterme işlemleri
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_other_layout, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        alertDialog.show();

    }


    private void otherNameGetir() {

        referenceName.child("KullaniciKayitBilgi").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                KullaniciKayitModel kayitModel = dataSnapshot.getValue(KullaniciKayitModel.class);

                other_profile_userName.setText(kayitModel.getUserName());
                other_NameText.setText(kayitModel.getIsim());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void otherProfilResmiGetir() {

        referenceProfilResmi.child("ProfileImages").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profilResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")) {
                    Picasso.get().load(profilResmiModel.getResim()).into(other_profile_photo);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void otherPtofilBilgiGetir() {

        referenceProfilBilgi.child("ProfilBilgi").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfilBilgiModel profilBilgiModel = dataSnapshot.getValue(ProfilBilgiModel.class);

                other_bioText.setText(profilBilgiModel.getBio());
                other_websiteText.setText(profilBilgiModel.getWebsite());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void IstekAt(String userID, String otherID) {

        refIstek.child("Istekler").child(otherID).child(userID).child("istek").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Kontrol = "istek";
                    takipEt_Button.setBackgroundResource(R.drawable.button_profile_background);
                    takipEt_Button.setText("İstek Gönderildi");
                    takipEt_Button.setTextColor(Color.BLACK);
                    Toast.makeText(getContext(), "İstek Gönderildi", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Hata", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    void KontrolIstek() {
        refIstek.child("Istekler").child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(auth.getUid())) {
                    Kontrol = "istek";
                    takipEt_Button.setBackgroundResource(R.drawable.button_profile_background);
                    takipEt_Button.setText("İstek Gönderildi");
                    takipEt_Button.setTextColor(Color.BLACK);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IstekGeriCek(String userID, String otherID) {

        refIstek.child("Istekler").child(otherID).child(userID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(Void aVoid) {
                Kontrol = "";
                takipEt_Button.setBackgroundResource(R.drawable.button_takip_et);
                takipEt_Button.setText("Takip Et");
                takipEt_Button.setTextColor(Color.WHITE);
                Toast.makeText(getContext(), "İstek İptal Edildi", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void TakipKontrol() {

        refTakip.child("Takip").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherID)) {
                    Kontrol = "takip";
                    takipEt_Button.setBackgroundResource(R.drawable.button_profile_background);
                    takipEt_Button.setText("Takip");
                    takipEt_Button.setTextColor(Color.BLACK);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void TakipSayi() {

        final List<String> takipSayiList = new ArrayList<>();

        reference.child("Takip").child(otherID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                takipSayiList.add(dataSnapshot.getKey());
                other_takipSayiText.setText(String.valueOf(takipSayiList.size()));


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                takipSayiList.remove(dataSnapshot.getKey());
                other_takipSayiText.setText(String.valueOf(takipSayiList.size()));
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

        reference.child("Takipçi").child(otherID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                takipçiList.add(dataSnapshot.getKey());
                other_takipçiSayiText.setText(String.valueOf(takipçiList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                takipçiList.remove(dataSnapshot.getKey());
                other_takipçiSayiText.setText(String.valueOf(takipçiList.size()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderiSayisiGetir(){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    HomeModel homeModel=snapshot.getValue(HomeModel.class);
                    if (homeModel.getId().equals(otherID)){
                        i++;
                    }
                }
                other_gonderiSayiText.setText(String.valueOf(i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void HorRecAdd() {

        reference.child("ProfileImages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (!dataSnapshot.getKey().equals(auth.getUid()) && !dataSnapshot.getKey().equals(otherID)) {
                    horList.add(dataSnapshot.getKey());
                    horRecAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
