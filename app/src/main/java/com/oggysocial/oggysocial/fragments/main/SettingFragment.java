package com.oggysocial.oggysocial.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.AuthActivity;
import com.oggysocial.oggysocial.services.UserService;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingFragment extends Fragment {

    CardView btnLogout, btnChangePass, btnEditInfo;
    CircleImageView civAvatar;
    TextView tvUsername;
    View v;

    public SettingFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews();
        initListeners();
        return v;
    }

    private void initViews() {
        btnLogout = v.findViewById(R.id.btnLogout);
        btnEditInfo = v.findViewById(R.id.btnEditInfo);
        btnChangePass = v.findViewById(R.id.btnChangePassword);
        civAvatar = v.findViewById(R.id.civAvatar);
        tvUsername = v.findViewById(R.id.tvUsername);

        UserService.getUser(user -> {
            if (user.getAvatar() != null) {
                Glide.with(requireContext()).load(user.getAvatar()).into(civAvatar);
                tvUsername.setText(user.getFullName());
            }
        });

    }

    private void initListeners() {
        btnLogout.setOnClickListener(v -> {
            logout();
        });
        btnEditInfo.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, BasicInfo.class, null)
                    .commit();
        });

        btnChangePass.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, ChangePasswordFragment.class, null)
                    .commit();
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        UserService.user = null;
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        v = null;
    }
}