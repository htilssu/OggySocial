package com.oggysocial.oggysocial.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.AuthActivity;
import com.oggysocial.oggysocial.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    AuthActivity authActivity;
    View rootView;

    public RegisterFragment() {
        this.authActivity = AuthActivity.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        navigateRegisterName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        TextView alreadyHaveAccount = rootView.findViewById(R.id.tvLogin);
        alreadyHaveAccount.setOnClickListener(v -> authActivity.navigateLogin());
    }

    public void navigateRegisterName() {
        getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.register_fragment_container, RegisterName.class, null)
                .commit();
    }

}