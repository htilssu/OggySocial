package com.oggysocial.oggysocial.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oggysocial.oggysocial.R;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigationView;

    Fragment homeFragment, profileFragment, settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initListener();
    }

    public void initVariables() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    public void initListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
            } else if (itemId == R.id.profile_item) {
            } else if (itemId == R.id.setting_item) {
            }
            return true;
        });
    }
}