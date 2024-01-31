package com.oggysocial.oggysocial.fragments.authentication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.MainActivity;
import com.oggysocial.oggysocial.controllers.UserController;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.EmailService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class EmailPasswordFragment extends Fragment {

    String email, password;
    TextInputLayout teEmailLayout, tePasswordLayout;
    Button btnNext;
    TextInputEditText teEmail, tePassword, teConfirmPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables(view);
        initListener();
    }

    private void initListener() {
        btnNext.setOnClickListener(v -> {
            if (validateEmail() && validatePassword()) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("App", MODE_PRIVATE);
                String firstName = sharedPreferences.getString("firstName", "");
                String lastName = sharedPreferences.getString("lastName", "");
                String birthday = sharedPreferences.getString("birthday", "");
                User user = new User(firstName, lastName, email, birthday);
                if (UserController.registerUser(getContext(), user, password)) {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

        });
    }


    private void initVariables(View view) {
        teEmailLayout = view.findViewById(R.id.teEmailLayout);
        tePasswordLayout = view.findViewById(R.id.tePasswordLayout);
        teEmail = view.findViewById(R.id.teEmail);
        tePassword = view.findViewById(R.id.tePassword);
        teConfirmPassword = view.findViewById(R.id.teConfirmPassword);
        btnNext = view.findViewById(R.id.btnNext);
    }

    private boolean validateEmail() {
        email = Objects.requireNonNull(teEmail.getText()).toString();
        if (email.isEmpty()) {
            teEmailLayout.setError(getString(R.string.required_email));
            return false;
        } else if (!EmailService.isValidEmail(email)) {
            teEmailLayout.setError(getString(R.string.invalid_email));
            return false;
        }
        teEmailLayout.setError(null);
        return true;
    }

    private boolean validatePassword() {
        password = Objects.requireNonNull(tePassword.getText()).toString();
        if (password.isEmpty()) {
            tePasswordLayout.setError(getString(R.string.required_password));
            return false;
        } else if (password.length() < 8 || password.length() > 32) {
            tePasswordLayout.setError(getString(R.string.invalid_password));
            return false;
        }
        tePasswordLayout.setError(null);
        return true;
    }
}