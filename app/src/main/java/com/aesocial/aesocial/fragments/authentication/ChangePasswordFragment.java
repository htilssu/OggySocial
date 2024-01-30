package com.aesocial.aesocial.fragments.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aesocial.aesocial.R;


public class ChangePasswordFragment extends Fragment {
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return view;
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confirmNewPassword = etConfirmNewPassword.getText().toString();

        if (validatePassword(currentPassword, newPassword, confirmNewPassword)) {
            // Thực hiện đổi mật khẩu
            // Gửi yêu cầu đến server hoặc cập nhật trong cơ sở dữ liệu
        }
    }

    private boolean validatePassword(String current, String newPass, String confirmNewPass) {
        // Thêm logic kiểm tra mật khẩu ở đây
        // Ví dụ: kiểm tra mật khẩu không trống, mật khẩu mới và xác nhận mật khẩu phải trùng nhau, v.v.
        return true;
    }
}