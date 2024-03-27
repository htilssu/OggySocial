package com.oggysocial.oggysocial.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.HomeFragment;
import com.oggysocial.oggysocial.fragments.main.NotifyFragment;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.fragments.main.SettingFragment;
import com.oggysocial.oggysocial.services.DialogService;
import com.oggysocial.oggysocial.services.ImageService;
import com.oggysocial.oggysocial.services.UserService;

import java.io.IOException;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "OGGY_SOCIAL";
    static WeakReference<MainActivity> instance;
    FrameLayout appBarLayout;
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment, profileFragment, settingFragment, notifyFragment;


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
        checkBlock();

        createNotificationChannel();

        pickImage = ImageService.getPickMedia(this, result -> {
            if (result != null) {
                CircleImageView civAvatar = findViewById(R.id.civAvatar);
                Glide.with(this).load(result).into(civAvatar);

                try {
                    ImageService.uploadImageSquare(this, result, ref -> {
                        UserService.getUser(user -> {
                            ref.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                user.setAvatar(uri1.toString());
                                UserService.updateUser(user);
                            });
                        });
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


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
        notifyFragment = new NotifyFragment();
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

            } else if (itemId == R.id.notification_item) {
                showFragment(notifyFragment);

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

    private void createNotificationChannel() {

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void checkBlock() {
        UserService.getUserRealTime(user -> {
            if (user.getBlocked()) {
                DialogService.showDialog(this, "Tài khoản của bạn đã bị khóa", "Vui lòng liên hệ với quản trị viên để biết thêm chi tiết", "Đóng", (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

}