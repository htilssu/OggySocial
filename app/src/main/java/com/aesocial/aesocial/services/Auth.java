package com.aesocial.aesocial.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;


public class Auth {

    private static final String host = "http://localhost:8080";
    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user;

    static public boolean login(String userName, String password) {
        AtomicBoolean isSuccess = new AtomicBoolean(false);
        if (userName.isEmpty() || password.isEmpty()) return false;
        Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(userName, password);
        authResultTask.addOnSuccessListener(authResult -> {
            sendAuthenticationToken(authResult.getUser());
            isSuccess.set(true);
        });

        return isSuccess.get();
    }

    static public void register(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) return;
        Task<AuthResult> authResultTask = firebaseAuth.createUserWithEmailAndPassword(userName, password);
        authResultTask.addOnSuccessListener(authResult -> {
            sendAuthenticationToken(authResult.getUser());
        });

    }

    static public boolean logOut() {
        firebaseAuth.signOut();
        return true;
    }

    static public boolean isLogin() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private static void sendAuthenticationToken(FirebaseUser user) {
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    sendTokenToServer(idToken);
                }
            });
        }
    }

    private static void sendTokenToServer(String idToken) {
        try {
            URL url = new URL(host + "/auth/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + idToken);
            int responseCode = connection.getResponseCode();
            String content = connection.getResponseMessage();
        } catch (IOException e) {
            // Handle exception
        }
    }

    public FirebaseUser getUser() {
        return user;
    }

}
