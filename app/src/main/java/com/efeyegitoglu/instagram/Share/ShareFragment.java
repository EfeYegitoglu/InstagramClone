package com.efeyegitoglu.instagram.Share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.efeyegitoglu.instagram.R;
import com.google.android.material.tabs.TabLayout;


public class ShareFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_share, container, false);

        slidePager();

        return view;
    }

    private void slidePager(){
        ViewPager viewPager = view.findViewById(R.id.share_viewPager);
        SlidePagerAdapter adapter = new SlidePagerAdapter(getChildFragmentManager(), 0);
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new ShareCameraFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout=view.findViewById(R.id.share_tab);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Galeri");
        tabLayout.getTabAt(1).setText("Fotoğraf");

    }
}
