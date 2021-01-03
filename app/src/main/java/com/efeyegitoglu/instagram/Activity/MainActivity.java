package com.efeyegitoglu.instagram.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.efeyegitoglu.instagram.Home.HomeAdapterFragment;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.efeyegitoglu.instagram.Notification.NotificationFragment;
import com.efeyegitoglu.instagram.Profile.ProfileFragment;
import com.efeyegitoglu.instagram.Search.SearchFragment;
import com.efeyegitoglu.instagram.Share.ShareFragment;
import com.efeyegitoglu.instagram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    ChangeFragment changeFragment;

    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar();
        kullaniciKontrol();


    }

    void bottomNavigationBar() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        //animasyon iptalleri


        //icon basımı sayfa değişikliği

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.home){

                    changeFragment=new ChangeFragment(MainActivity.this);
                    changeFragment.change(new HomeAdapterFragment());
                }
                if (id == R.id.search) {
                    changeFragment=new ChangeFragment(MainActivity.this);
                    changeFragment.changeCanBack(new SearchFragment());
                }
                if (id == R.id.share) {
                    changeFragment=new ChangeFragment(MainActivity.this);
                    changeFragment.changeCanBack(new ShareFragment());
                }
                if (id == R.id.notification) {
                    changeFragment=new ChangeFragment(MainActivity.this);
                    changeFragment.changeCanBack(new NotificationFragment());
                }
                if (id == R.id.profile) {
                    changeFragment=new ChangeFragment(MainActivity.this);
                    changeFragment.changeCanBack(new ProfileFragment());
                }
                return true;

            }
        });

        //ilk seçili icon
        bottomNavigationViewEx.setSelectedItemId(R.id.home);

    }

    void kullaniciKontrol(){

        if (firebaseUser==null){
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
