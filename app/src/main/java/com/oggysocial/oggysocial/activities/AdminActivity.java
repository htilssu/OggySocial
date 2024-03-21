package com.oggysocial.oggysocial.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.databinding.ActivityAdminBinding;
import com.oggysocial.oggysocial.fragments.admin.HomeAdminFragment;

public class AdminActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navigationView);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content_admin);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_admin);

        mAppBarConfiguration = new AppBarConfiguration.Builder(navGraph).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initView();
    }

    private void initView() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}