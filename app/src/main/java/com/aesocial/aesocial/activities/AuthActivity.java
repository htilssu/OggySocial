package com.aesocial.aesocial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import com.aesocial.aesocial.R;
import com.aesocial.aesocial.fragments.authentication.LoginFragment;
import com.aesocial.aesocial.fragments.authentication.RegisterFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class AuthActivity extends AppCompatActivity {

    public static AuthActivity instance;
    private final String PREFERENCE = "AESocial";
    private MaterialToolbar toolbar;

    public static AuthActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initField();
        checkLogin();
        initListeners();
    }

    private void initField() {
        toolbar = findViewById(R.id.toolbar_auth);
        toolbar.setNavigationOnClickListener(v -> {
            Log.i("AAA", "initField: onNavigationClick");
            onBackNavigationClick();
        });
    }

    public void navigateLogin() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new LoginFragment())
                .commit();
//        setSupportActionBar(null);
    }

    public void navigateMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void navigateRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new RegisterFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }


    private void checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            navigateMain();
        } else {
            navigateLogin();
        }
    }

    public void showActionBar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
    }

    private void initListeners() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(() -> {
            if (fragmentManager.getBackStackEntryCount() > 0 && getSupportActionBar() == null) {
                showActionBar();
            } else {
                hideActionBar();
            }
        });
    }

    private void onBackNavigationClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}