package com.aesocial.aesocial;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aesocial.aesocial.databinding.ActivityMainBinding;
import com.aesocial.aesocial.databinding.FragmentLoginBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        loginBinding = FragmentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
