package com.oggysocial.oggysocial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialToolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    DrawerLayout drawerLayout;

    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        drawerLayout = findViewById(R.id.drawerLayout);

        toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.fragment_content_admin);

        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_admin);

        mAppBarConfiguration = new AppBarConfiguration.Builder(navGraph).setOpenableLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);


        initView();
    }

    private void initView() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent toMain = new Intent(getApplicationContext(), AuthActivity.class);
            toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toMain);
        } else if (menuItem.getItemId() == R.id.nav_user) {
            navController.navigate(R.id.nav_user);
        }
        drawerLayout.close();

        return true;
    }

}
