package com.oggysocial.oggysocial.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oggysocial.oggysocial.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class UserService {

    public static User user;
    private static List<User> userList;

    /**
     * Lấy thông tin user theo tên
     *
     * @param listener callback trả về {@link List<User>} thông tin người dùng
     */

    public static void getAllUser(OnListUserLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                userList = queryDocumentSnapshots.toObjects(User.class);
                listener.onListUserLoaded(userList);
            }
        });
    }

    public static void getUserByName(String name, OnListUserLoadedListener listener) {
        if (name.isBlank()) {
            listener.onListUserLoaded(null);
            return;
        }

        if (userList == null) {
            getAllUser(users -> {
                List<User> result = new ArrayList<>(userList);
                result.removeIf(user -> !user.getFullName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)) || user.getId().equals(FirebaseAuth.getInstance().getUid()));
                listener.onListUserLoaded(result);
            });
        } else {
            List<User> result = new ArrayList<>(userList);
            result.removeIf(user -> !user.getFullName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)) || user.getId().equals(FirebaseAuth.getInstance().getUid()));
            listener.onListUserLoaded(result);
        }


    }

    /**
     * Lấy thông tin của user hiện tại
     *
     * @param listener hàm callback trả về người dùng được load
     */
    public static void getUser(OnUserLoadedListener listener) {

        if (user != null && Objects.equals(user.getId(), FirebaseAuth.getInstance().getUid())) {
            listener.onUserLoaded(user);
        } else {
            getUserById(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()), user -> {
                UserService.user = user;
                listener.onUserLoaded(user);
            });
        }

    }

    public static void getUserRealTime(Consumer<User> user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addSnapshotListener((documentSnapshot, e) -> {

            if (documentSnapshot != null) {
                User u = documentSnapshot.toObject(User.class);
                user.accept(u);
            }
        });
    }

    /**
     * Lấy thông tin user theo id
     *
     * @param userId   id của người dùng cần lấy thông tin
     * @param listener callback trả về thông tin người dùng
     */
    public static void getUserById(String userId, OnUserLoadedListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            listener.onUserLoaded(user);
        });
    }

    /**
     * Lưu thông tin user
     *
     * @param user {@link User} cần lưu
     */
    public static void createUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(user);
    }

    public static void updateUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getId()).set(user);
        new Thread(() -> {
            PostService.getUserPosts(user.getId(), posts -> {
                posts.forEach(post -> {
                    post.setUser(user);
                    PostService.updatePost(post);
                });
            });
        }).start();
    }

    /**
     * Lấy danh sách bạn bè của user
     *
     * @param userId   id của người dùng
     * @param listener callback trả về danh sách bạn bè
     */
    public static void getFriends(String userId, OnListUserLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null) {
                List<String> listFriendId = user.getFriends();
                if (listFriendId.isEmpty()) {
                    listener.onListUserLoaded(null);
                } else {
                    db.collection("users").whereIn("id", listFriendId).get().addOnSuccessListener(queryDocumentSnapshots -> {
                        List<User> friends = queryDocumentSnapshots.toObjects(User.class);
                        listener.onListUserLoaded(friends);
                    });
                }
            }
        });
    }

    public static void resetPassword(String email, OnEmailSentListener listener) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            listener.onEmailSent();
        });
    }

    public static void checkPassword(String password, Consumer<Boolean> callback) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    callback.accept(true);
                }).addOnFailureListener(e -> {
                    callback.accept(false);
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


    public interface OnEmailSentListener {
        void onEmailSent();
    }


}
