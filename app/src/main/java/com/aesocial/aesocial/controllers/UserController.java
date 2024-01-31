package com.aesocial.aesocial.controllers;

import android.content.Context;

import com.aesocial.aesocial.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserController {

    public static boolean registerUser(Context context, User user, String password) {
        FirebaseAuth firebase = FirebaseAuth.getInstance();
        Task<AuthResult> authResult = firebase.createUserWithEmailAndPassword(user.getEmail(), password);
        logOut();
        authResult.addOnSuccessListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("users").child(Objects.requireNonNull(firebase.getCurrentUser()).getUid()).setValue(user);
        });
        authResult.addOnFailureListener(e -> {
            throw new RuntimeException(e.getMessage());
        });
        try {
            synchronized (authResult) {
                authResult.wait(5000);
            }
        } catch (InterruptedException e) {
            return false;
        }
        FirebaseUser loginUser = firebase.getCurrentUser();
        return loginUser != null;
    }

    public static boolean logOut() {
        FirebaseAuth.getInstance().signOut();
        return true;
    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
