package com.efeyegitoglu.instagram.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.efeyegitoglu.instagram.Search.SearchFragment;
import com.efeyegitoglu.instagram.Share.ShareFragment;

import java.util.ArrayList;
import java.util.List;

public class PageSlideAdapter extends FragmentPagerAdapter {

    private  final List<Fragment> fragmentList=new ArrayList<>();


    public PageSlideAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);

    }
}
