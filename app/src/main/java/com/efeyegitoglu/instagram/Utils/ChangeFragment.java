package com.efeyegitoglu.instagram.Utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Search.AraModel;

public class ChangeFragment {

    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;

    }

    public void changeCanBack(Fragment fragment) {

        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null)
                .commit();

    }


    public void changeWithParameter(Fragment fragment, String userId){

        Bundle bundle=new Bundle();
        bundle.putString("other_userID",userId);
        fragment.setArguments(bundle);


        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();

    }


    public void change(Fragment fragment) {

        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();

    }

    public  void changeWithParameterCanBack(Fragment fragment, String userId){
        Bundle bundle=new Bundle();
        bundle.putString("other_userID",userId);
        fragment.setArguments(bundle);


        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null)
                .commit();

    }


}
