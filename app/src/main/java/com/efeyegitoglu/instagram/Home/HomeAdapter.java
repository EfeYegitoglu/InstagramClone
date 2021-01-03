package com.efeyegitoglu.instagram.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.OtherProfileFragment;
import com.efeyegitoglu.instagram.Profile.ProfileFragment;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    List<HomeModel> modelList;
    Context context;
    FirebaseAuth auth;

    public HomeAdapter(List<HomeModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paylasim_layout, parent, false);
        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        auth = FirebaseAuth.getInstance();

        final HomeModel homeModel=modelList.get(position);

        Picasso.get().load(homeModel.getResim()).into(holder.PostImage);

        if (homeModel.getComment().isEmpty()){
            holder.paylasimAciklama.setVisibility(View.GONE);
            holder.paylasimYorumName.setVisibility(View.GONE);
        }else {
            holder.paylasimAciklama.setVisibility(View.VISIBLE);
            holder.paylasimYorumName.setVisibility(View.VISIBLE);
            holder.paylasimAciklama.setText(homeModel.getComment());

        }

        idBilgiAl(holder.paylasimPhoto,holder.paylasimUserName,homeModel.getId(),holder.paylasimYorumName);

        begenilerPost(homeModel.getPostid(),holder.paylasimBegen);
        begeniSayisi(holder.paylasimBegeniSayisi,homeModel.getPostid());
        yorumSayisiGör(holder.paylasimYorumGor,homeModel.getPostid());

        holder.paylasimBegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.paylasimBegen.getTag().equals("begen")){
                    FirebaseDatabase.getInstance().getReference("Begeniler").child(homeModel.getPostid())
                            .child(auth.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference("Begeniler").child(homeModel.getPostid())
                            .child(auth.getUid()).removeValue();
                }
            }
        });



        holder.paylasimYorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(context);
                changeFragment.changeWithParameterCanBack(new YorumFragment(),homeModel.getPostid());
            }
        });

        holder.paylasimYorumGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(context);
                changeFragment.changeWithParameterCanBack(new YorumFragment(),homeModel.getPostid());
            }
        });

        holder.paylasimUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeModel.getId().equals(auth.getUid())) {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new ProfileFragment(),homeModel.getId());
                }else {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),homeModel.getId());
                }
            }
        });
        holder.paylasimPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeModel.getId().equals(auth.getUid())) {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new ProfileFragment(),homeModel.getId());
                }else {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),homeModel.getId());
                }
            }
        });
        holder.paylasimYorumName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeModel.getId().equals(auth.getUid())) {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new ProfileFragment(),homeModel.getId());
                }else {
                    ChangeFragment changeFragment=new ChangeFragment(context);
                    changeFragment.changeWithParameterCanBack(new OtherProfileFragment(),homeModel.getId());
                }
            }
        });

        holder.paylasimOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    //Dialog gösterme işlemleri
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.dialog_other_layout, null);

                    AlertDialog alertDialog = new AlertDialog.Builder(context).setView(view).create();
                    alertDialog.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView paylasimPhoto;
        TextView paylasimUserName, paylasimBegeniSayisi, paylasimYorumName, paylasimAciklama, paylasimYorumGor;
        ImageView paylasimOptions, paylasimBegen, paylasimYorum, paylasimGonder, paylasimKaydet, PostImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paylasimPhoto = itemView.findViewById(R.id.paylasimPhoto);
            paylasimBegen = itemView.findViewById(R.id.paylasimBegen);
            paylasimYorum = itemView.findViewById(R.id.paylasimYorum);
            PostImage = itemView.findViewById(R.id.PostImage);
            paylasimUserName = itemView.findViewById(R.id.paylasimUserName);
            paylasimBegeniSayisi = itemView.findViewById(R.id.paylasimBegeniSayisi);
            paylasimYorumName = itemView.findViewById(R.id.paylasimYorumName);
            paylasimAciklama = itemView.findViewById(R.id.paylasimAciklama);
            paylasimYorumGor = itemView.findViewById(R.id.paylasimYorumGor);
            paylasimOptions = itemView.findViewById(R.id.paylasimOptions);

        }
    }

    private void idBilgiAl(final CircleImageView profilResmi, final TextView userName, String userId, final TextView aciklamaName) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileImages").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profileResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profileResmiModel.getResim().isEmpty()){
                    Picasso.get().load(profileResmiModel.getResim()).into(profilResmi);
                }
                userName.setText(profileResmiModel.getUserName());
                aciklamaName.setText(profileResmiModel.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void begenilerPost(String postid, final ImageView imageView){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Begeniler").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(auth.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_action_like);
                    imageView.setTag("begenildi");
                }else {
                    imageView.setImageResource(R.drawable.ic_action_unlike);
                    imageView.setTag("begen");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void begeniSayisi(final TextView begeniler, String postid){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Begeniler").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                begeniler.setText(dataSnapshot.getChildrenCount()+" "+"beğenme");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void yorumSayisiGör(final TextView yorumlar, String postId){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Yorumlar").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() != 0) {
                    yorumlar.setVisibility(View.VISIBLE);
                    yorumlar.setText(dataSnapshot.getChildrenCount()+" "+"yorumun tümünü gör...");
                }else {
                    yorumlar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
