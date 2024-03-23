package com.oggysocial.oggysocial.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.services.UserService;

import java.util.Objects;

public class ChangePasswordFragment extends Fragment {

    MaterialButton btnChangePassword;
    TextInputLayout telOldPassword;
    TextInputEditText teOldPassword, teNewPassword;
    ImageView ivBack;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListener();
    }

    private void initView() {
        btnChangePassword = requireView().findViewById(R.id.btnChangePassword);
        telOldPassword = requireView().findViewById(R.id.telOldPassword);
        teOldPassword = requireView().findViewById(R.id.teOldPassword);
        teNewPassword = requireView().findViewById(R.id.teNewPassword);
        ivBack = requireView().findViewById(R.id.ivBack);
    }

    private void initListener() {
        btnChangePassword.setOnClickListener(v -> changePassword());
        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void changePassword() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String oldPassword = Objects.requireNonNull(teOldPassword.getText()).toString();
        String newPassword = Objects.requireNonNull(teNewPassword.getText()).toString();
        if (oldPassword.equals(newPassword)) {
            telOldPassword.setError("Mật khẩu mới không được giống mật khẩu cũ");
            return;
        }
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            telOldPassword.setError(getString(R.string.required_info));
            return;
        }

        UserService.checkPassword(oldPassword, isCorrect -> {
            if (isCorrect) {
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updatePassword(newPassword).addOnSuccessListener(command
                        -> {
                    Toast.makeText(requireContext(), R.string.change_pass_success, Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });

            } else {
                telOldPassword.setError("Sai mật khẩu");
            }
        });
    }
}