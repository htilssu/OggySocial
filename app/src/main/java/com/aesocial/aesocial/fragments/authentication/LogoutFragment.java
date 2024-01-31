package com.aesocial.aesocial.fragments.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aesocial.aesocial.R;


public class LogoutFragment extends Fragment {
    private Button logoutButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return view;
    }

    private void logoutUser() {

        SharedPreferences preferences = getActivity().getSharedPreferences("Tên bạn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển đến màn hình đăng nhập
        Intent intent = new Intent(getActivity(), LoginFragment.class);
        startActivity(intent);
        getActivity().finish();
    }
}