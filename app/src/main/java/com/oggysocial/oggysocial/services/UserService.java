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
     * Lấy thông tin của user hiện tại
     *
     * @param listener hàm callback trả về người dùng được load
     */
    public static void getUser(OnUserLoadedListener listener) {

        getUserById(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()), listener);
    }

    /**
     * Lấy thông tin user theo id
     *
     * @param userId   id của người dùng cần lấy thông tin
     * @param listener callback trả về thông tin người dùng
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


    /**
     * Lưu thông tin user
     *
     * @param user {@link User} cần lưu
     */
    public static void saveUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(user);
    }

    /**
     * Lấy danh sách bạn bè của user
     *
     * @param userId   id của người dùng
     * @param listener callback trả về danh sách bạn bè
     */
    public static void getFriends(String userId, OnListUserLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("friends").get().addOnSuccessListener(queryDocumentSnapshots -> {
            listener.onListUserLoaded(queryDocumentSnapshots.toObjects(User.class));
        });
    }

    /**
     * Callback khi thông tin user được load
     */
    public interface OnUserLoadedListener {

        void onUserLoaded(User user);

    }

    /**
     * Callback khi danh sách user được load
     */
    public interface OnListUserLoadedListener {
        void onListUserLoaded(List<User> users);
    }


}
