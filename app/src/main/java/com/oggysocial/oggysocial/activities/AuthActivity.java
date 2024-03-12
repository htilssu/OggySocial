package com.oggysocial.oggysocial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.auth.ForgotPasswordFragment;
import com.oggysocial.oggysocial.fragments.auth.LoginFragment;
import com.oggysocial.oggysocial.fragments.auth.RegisterFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.oggysocial.oggysocial.services.UserService;

public class AuthActivity extends AppCompatActivity {

    public static AuthActivity instance;
    private final String PREFERENCES = "App";
    private MaterialToolbar toolbar;

    public static AuthActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
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
            onBackNavigationClick();
        });
    }

    public void navigateLogin() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new LoginFragment())
                .commit();
    }

    public void navigateMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }

    public void navigateRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, new RegisterFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }


    private void checkLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean isLoggedIn = user != null;
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
            if (fragmentManager.getBackStackEntryCount() > 0) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }


}