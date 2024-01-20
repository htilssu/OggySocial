package com.aesocial.aesocial.fragments.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aesocial.aesocial.R;
import com.aesocial.aesocial.activities.AuthActivity;
import com.aesocial.aesocial.databinding.FragmentLoginBinding;
import com.aesocial.aesocial.services.Auth;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    
    FragmentLoginBinding binding;
    AuthActivity authActivity;

    View rootView;

    public LoginFragment() {
        this.authActivity = AuthActivity.getInstance();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }


    private void initListeners() {
        //Login Button
        Button loginButton = rootView.findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(v -> onLoginClick());

        //Register Button
        Button registerButton = rootView.findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(v -> authActivity.navigateRegister());
    }

    private void onLoginClick() {
        TextInputEditText tipUserName = rootView.findViewById(R.id.tipUserName);
        TextInputEditText tipPassword = rootView.findViewById(R.id.tipPassword);
        Editable userName = tipUserName.getText();
        Editable password = tipPassword.getText();
        if (userName == null || password == null) return;
        String userNameString = userName.toString();
        String passwordString = password.toString();

        if (Auth.login(userNameString, passwordString)) {
            authActivity.navigateMain();
            authActivity.finish();
        }

    }


}
