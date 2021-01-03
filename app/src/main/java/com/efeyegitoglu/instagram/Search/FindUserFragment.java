package com.efeyegitoglu.instagram.Search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FindUserFragment extends Fragment {
    View view;
    EditText find_user_editText;
    ImageView turn_back;

    DatabaseReference refUserName, refPhoto;
    FirebaseAuth auth;

    List<AraModel> mList;
    RecyclerView findUser_Recycler;
    FindUserAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_find_user_search, container, false);


        tanimlamalar();
        //kullaniciGetir();


        return view;
    }

    void tanimlamalar() {
        turn_back = view.findViewById(R.id.turn_back);
        turn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new SearchFragment());
            }
        });


        auth = FirebaseAuth.getInstance();


        mList = new ArrayList<>();

        findUser_Recycler = view.findViewById(R.id.findUser_Recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        findUser_Recycler.setLayoutManager(layoutManager);
        adapter=new FindUserAdapter(mList,getContext());
        findUser_Recycler.setAdapter(adapter);


        LoadUser();

        find_user_editText = view.findViewById(R.id.find_user_editText);
        find_user_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SearchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void SearchUser(final java.lang.String s){

        Query sorgu = FirebaseDatabase.getInstance().getReference("ProfileImages").orderByChild("userName")
                .startAt(s)
                .endAt(s+"\uf8ff");

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AraModel araModel = snapshot.getValue(AraModel.class);
                    mList.add(araModel);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void LoadUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileImages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (find_user_editText.getText().toString().isEmpty()) {
                    mList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AraModel araModel = snapshot.getValue(AraModel.class);
                        mList.add(araModel);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
