package com.oggysocial.oggysocial.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.fragments.main.HomeFragment;
import com.oggysocial.oggysocial.fragments.main.SettingFragment;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;
    Fragment homeFragment, profileFragment, settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
                showFragment(homeFragment);
            } else if (itemId == R.id.profile_item) {
                showFragment(profileFragment);
            } else if (itemId == R.id.setting_item) {
                showFragment(settingFragment);
            }
            return true;
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, fragment).commit();
    }
}