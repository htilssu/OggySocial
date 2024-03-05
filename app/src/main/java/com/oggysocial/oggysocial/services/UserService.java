package com.oggysocial.oggysocial.services;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oggysocial.oggysocial.models.User;

import java.util.List;
import java.util.Objects;

public class UserService {

    static User user;

    /**
     * Lấy thông tin user hiện tại
     */
    public static void getUser(OnUserLoadedListener listener) {

        getUserById(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()), listener);
    }

    /**
     * Lấy thông tin user theo id
     *
     * @param userId   id của user
     * @param listener callback
     */
    public static void getUserById(String userId, OnUserLoadedListener listener) {

        if (user == null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                user = documentSnapshot.toObject(User.class);
                listener.onUserLoaded(user);
            });
        } else {
            listener.onUserLoaded(user);
        }
    }

    public static Task<User> getUserByIdAsync(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").document(userId).get().continueWith(task -> {
            if (task.isSuccessful()) {
                return task.getResult().toObject(User.class);
            } else {
                Log.e("UserService", "getUserByIdAsync: ", task.getException());
                return null;
            }
        });
    }

    /**
     * Lưu thông tin user
     *
     * @param user thông tin user
     */
    public static void saveUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(user);
    }

    public static void getFriends(String userId, OnListUserLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("friends").get().addOnSuccessListener(queryDocumentSnapshots -> {
            listener.onListUserLoaded(queryDocumentSnapshots.toObjects(User.class));
        });
    }

    public interface OnUserLoadedListener {
        void onUserLoaded(User user);

    }

    public interface OnListUserLoadedListener {
        void onListUserLoaded(List<User> users);
    }


}
