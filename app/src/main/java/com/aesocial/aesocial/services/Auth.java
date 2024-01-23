package com.aesocial.aesocial.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class Auth {

    private static final String host = "http://10.0.2.2:8080";
    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user;

    static public CompletableFuture<Boolean> login(String userName, String password) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        if (userName.isEmpty() || password.isEmpty()) {
            completableFuture.complete(false);
            return completableFuture;
        }
        Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(userName, password);
        authResultTask.addOnSuccessListener(authResult -> {
            sendAuthenticationToken(authResult.getUser());
            completableFuture.complete(true);
        });

        return completableFuture;
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
            Task<GetTokenResult> resultTask = user.getIdToken(true);
            resultTask.addOnCompleteListener(task -> {
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
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FirebaseUser getUser() {
        return user;
    }

}
