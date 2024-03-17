package com.oggysocial.oggysocial.activities;

import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.HomeFragment;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.fragments.main.SettingFragment;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    static WeakReference<MainActivity> instance;
    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment;
    Fragment profileFragment;
    Fragment settingFragment;

    public static WeakReference<MainActivity> getInstance() {
        return instance;
    }

    public Fragment getHomeFragment() {
        return homeFragment;
    }

    public Fragment getProfileFragment() {
        return profileFragment;
    }

    public Fragment getSettingFragment() {
        return settingFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = new WeakReference<>(this);

        setContentView(R.layout.activity_main);

        initTransition();
        initVariables();
        initListener();
        initFragments();
        showFragment(homeFragment);
    }


    private void initFragments() {
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        settingFragment = new SettingFragment();
    }

    private void initVariables() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        appBarLayout = findViewById(R.id.appBarLayout);
    }

    private void initListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
                showFragment(homeFragment);
            } else if (itemId == R.id.profile_item) {
                ((ProfileFragment) profileFragment).setShowAppBar(false);
                showFragment(profileFragment);
            } else if (itemId == R.id.setting_item) {
                showFragment(settingFragment);
            }
            return true;
        });

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }

    public void showProfile() {
        showFragment(profileFragment);
    }


    private void initTransition() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

}