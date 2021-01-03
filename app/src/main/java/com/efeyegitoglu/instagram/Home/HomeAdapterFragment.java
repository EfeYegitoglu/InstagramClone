package com.efeyegitoglu.instagram.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efeyegitoglu.instagram.Message.MessageFragment;
import com.efeyegitoglu.instagram.R;


public class HomeAdapterFragment extends Fragment {
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_adapter_home, container, false);

        homeAdapterYanaKaydirma();

        return view;
    }

    void homeAdapterYanaKaydirma() {

        //kamera,Home ve DM gösteriliyor. Kaydırarak geçiş var.

        ViewPager viewPager = view.findViewById(R.id.home_viewPager);
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager(), 0);
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessageFragment());
        viewPager.setAdapter(adapter);

    }
}
