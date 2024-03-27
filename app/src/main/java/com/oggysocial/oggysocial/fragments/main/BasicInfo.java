package com.oggysocial.oggysocial.fragments.main;

import static com.oggysocial.oggysocial.services.UserService.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.UserService;

import java.util.Locale;
import java.util.Objects;


public class BasicInfo extends Fragment {
    private TextInputEditText teLastName;
    private TextInputEditText teFirstName;
    private Button btnNext;
    private DatePicker datePicker;

    public BasicInfo() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserService.updateUser(user);

        teLastName = view.findViewById(R.id.teLastName);
        teFirstName = view.findViewById(R.id.teFirstName);
        btnNext = view.findViewById(R.id.btnNext);
        datePicker =view.findViewById(R.id.datePicker);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

    }

    private void updateUser() {
        String lastName = Objects.requireNonNull(teLastName.getText()).toString().trim();
        String firstName = Objects.requireNonNull(teFirstName.getText()).toString().trim();

        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1; // Month is 0-indexed
        int day = datePicker.getDayOfMonth();
        String birthday = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);


        if (!lastName.isEmpty() && !firstName.isEmpty()) {
            // Lấy đối tượng User hiện tại
            User currentUser = UserService.user;

            // Kiểm tra xem đối tượng User có tồn tại hay không
            if (currentUser != null) {
                // Thiết lập giá trị mới cho trường firstName của đối tượng User
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setBirthday(birthday);
                // Cập nhật thông tin người dùng
                UserService.updateUser(currentUser);

                // Hiển thị thông báo cho người dùng
                Toast.makeText(getContext(), "Tên của bạn đã được cập nhật thành công.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Hiển thị thông báo lỗi nếu trường nhập liệu không được điền đầy đủ
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin họ và tên.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic_info, container, false);
    }
}