package com.oggysocial.oggysocial.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.AuthActivity;
import com.oggysocial.oggysocial.databinding.FragmentLoginBinding;

import java.util.Objects;

public class LoginFragment extends Fragment {


    FragmentLoginBinding binding;
    AuthActivity authActivity;
    String email, password;

    View rootView;

    public LoginFragment() {
        this.authActivity = AuthActivity.instance;
    }

    public void loginSuccess() {
        authActivity.navigateMain();
        authActivity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if (validateInput()) {
            login(email, password);
        }

    }

    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(v -> {
                    loginSuccess();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                });
    }

    private boolean validateInput() {
        TextInputEditText teEmail = requireView().findViewById(R.id.teEmail);
        TextInputEditText tePassword = requireView().findViewById(R.id.tePassword);
        email = Objects.requireNonNull(teEmail.getText()).toString();
        password = Objects.requireNonNull(tePassword.getText()).toString();
        if (email.isEmpty()) {
            TextInputLayout teEmailLayout = requireView().findViewById(R.id.teEmailLayout);
            teEmailLayout.setError(getString(R.string.required_email));
            return false;
        } else {
            TextInputLayout teEmailLayout = requireView().findViewById(R.id.teEmailLayout);
            teEmailLayout.setError(null);
        }
        if (password.isEmpty()) {
            TextInputLayout teEmailLayout = requireView().findViewById(R.id.tePasswordLayout);
            teEmailLayout.setError(getString(R.string.required_password));
            return false;
        } else {
            TextInputLayout teEmailLayout = requireView().findViewById(R.id.tePasswordLayout);
            teEmailLayout.setError(null);
        }

        return true;
    }

}
