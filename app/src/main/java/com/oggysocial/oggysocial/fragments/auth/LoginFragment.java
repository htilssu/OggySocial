package com.oggysocial.oggysocial.fragments.auth;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.AuthActivity;
import com.oggysocial.oggysocial.services.UserService;

import java.util.Objects;

public class LoginFragment extends Fragment {


    AuthActivity authActivity;
    String email, password;
    View rootView;
    TextView tvForgotPassword;
    Button btnLogin, btnRegister;
    LottieAnimationView lottieAnimationView;

    ConstraintLayout constraintLayout;


    public LoginFragment() {
        this.authActivity = AuthActivity.instance;
    }

    public void loginSuccess() {
        authActivity.navigateMain();
        requireView().findViewById(R.id.loadingLayout).setBackgroundColor(Color.TRANSPARENT);
        lottieAnimationView.cancelAnimation();
        authActivity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
    }

    private void initViews() {
        tvForgotPassword = requireView().findViewById(R.id.tvForgotPassword);
        btnLogin = requireView().findViewById(R.id.btnLogin);
        btnRegister = requireView().findViewById(R.id.btnRegister);
        lottieAnimationView = requireView().findViewById(R.id.animation_view);
        lottieAnimationView.setMinAndMaxFrame(0, 320);
    }

    private void initListeners() {
        //Login Button
        btnLogin.setOnClickListener(v -> onLoginClick());

        //Register Button
        btnRegister.setOnClickListener(v -> authActivity.navigateRegister());

        tvForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void navigateToForgotPassword() {

        getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.auth_fragment_container, new ForgotPasswordFragment())
                .commit();
    }

    private void onLoginClick() {
        if (validateInput()) {
            showLoading();
            login(email, password);
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }

    }

    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(v -> {
                    UserService.getUser(user -> {
                        UserService.user = user;
                    });
                    loginSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.i(TAG, "login: " + e.getMessage());
                    Toast.makeText(getContext(), "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }).addOnCompleteListener(task -> {
                    Log.i(TAG, "login: " + task.isSuccessful());
                }).addOnCanceledListener(() -> {
                    Log.i(TAG, "login: " + "canceled");
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

    private void showLoading() {
        lottieAnimationView.playAnimation();
        requireView().findViewById(R.id.loadingLayout).setBackgroundColor(requireContext().getResources().getColor(R.color.placeholder, null));
    }

}
