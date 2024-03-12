package com.oggysocial.oggysocial.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.services.EmailService;
import com.oggysocial.oggysocial.services.UserService;

import java.util.Objects;

public class ForgotPasswordFragment extends Fragment {

    TextInputEditText teEmail;
    MaterialButton btnResetPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
    }

    private void initViews() {
        teEmail = requireView().findViewById(R.id.teEmail);
        btnResetPassword = requireView().findViewById(R.id.btnResetPassword);
    }


    private void initListeners() {
        btnResetPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(teEmail.getText()).toString();
            if (email.isEmpty()) {
                teEmail.setError(getString(R.string.required_email));
                return;
            }
            EmailService.checkEmailExist(email, isExist -> {
                if (isExist) {
                    UserService.resetPassword(email, () -> {
                        requireActivity().getSupportFragmentManager().popBackStack();
                        Toast.makeText(requireContext(), R.string.reset_password_success, Toast.LENGTH_LONG).show();
                    });
                } else {
                    teEmail.setError(getString(R.string.email_not_exist));
                }
            });
        });
    }
}