package com.oggysocial.oggysocial.activities;

import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.HomeFragment;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.fragments.main.SettingFragment;
import com.oggysocial.oggysocial.services.ImageService;
import com.oggysocial.oggysocial.services.UserService;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    static WeakReference<MainActivity> instance;
    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment;
    Fragment profileFragment;
    Fragment settingFragment;

    ActivityResultLauncher<PickVisualMediaRequest> pickImage;

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

        pickImage = ImageService.getPickMedia(this, result -> {
            if (result != null) {
                ImageService.uploadImage(result, uri -> {
                    if (uri != null) {
                        CircleImageView civAvatar = findViewById(R.id.civAvatar);
                        Glide.with(this).load(uri).into(civAvatar);
                        ImageService.uploadImage(result, ref -> {
                            UserService.getUser(user -> {
                                ref.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                    user.setAvatar(uri1.toString());
                                    UserService.saveUser(user);
                                });
                            });
                        });
                    }
                });
            }
        }, null);

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

    public void showPickAvatarImage() {
        pickImage.launch(new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType("image/*")).build());
    }

}