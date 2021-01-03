package com.efeyegitoglu.instagram.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efeyegitoglu.instagram.Profile.OtherProfileFragment;
import com.efeyegitoglu.instagram.Profile.ProfileFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.ViewHolder> {

    List<AraModel> mList;
    Context context;

    FirebaseAuth auth;

    public FindUserAdapter(List<AraModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_user_layout, parent, false);
        return new FindUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final AraModel araModel = mList.get(position);


        holder.user_userName.setText(araModel.getUserName());

        holder.user_Name.setText(araModel.getIsim());

        if (!araModel.getResim().equals("")) {
            Picasso.get().load(araModel.getResim()).into(holder.user_photo);
        }

        holder.findUser_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (araModel.getId().equals(auth.getUid())) {
                    ChangeFragment changeFragment = new ChangeFragment(context);
                    changeFragment.changeCanBack(new ProfileFragment());

                } else {
                    ChangeFragment changeFragment = new ChangeFragment(context);
                    changeFragment.changeWithParameter(new OtherProfileFragment(),araModel.getId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_photo;
        TextView user_userName, user_Name;
        LinearLayout findUser_Layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_photo = itemView.findViewById(R.id.user_photo);
            user_userName = itemView.findViewById(R.id.user_userName);
            user_Name = itemView.findViewById(R.id.user_Name);
            findUser_Layout = itemView.findViewById(R.id.findUser_Layout);
        }
    }
}
