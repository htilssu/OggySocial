package com.oggysocial.oggysocial.services;

import static com.google.firebase.firestore.Filter.and;
import static com.google.firebase.firestore.Filter.equalTo;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oggysocial.oggysocial.models.FriendRequest;
import com.oggysocial.oggysocial.models.User;

import java.util.Date;
import java.util.List;

public class FriendService {
    public static void getRequest(Context context, OnRequestLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        CollectionReference friendReference = db.collection("friend_request");

        friendReference.whereEqualTo("receiverId", userId).addSnapshotListener((snapshot, err) -> {
            if (err != null) {
                return;
            }
            assert snapshot != null;
            List<FriendRequest> requests = snapshot.toObjects(FriendRequest.class);
            listener.onRequestLoaded(requests);
        });
    }

    public static void acceptRequest(FriendRequest request) {
        rejectRequest(request);

        addFriend(request.getSenderId());
    }


    public static void addFriend(String friendId) {

        //add friend to my self
        UserService.getUser(user -> {
            user.addFriend(friendId);
            UserService.updateUser(user);
        });

        //add friend to friend
        UserService.getUserById(friendId, user -> {
            user.addFriend(FirebaseAuth.getInstance().getUid());
            UserService.updateUser(user);
        });

    }

    public static void sendRequest(String userId, String friendId) {
        checkRequestExists(userId, friendId, exists -> {
            if (!exists) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference friendReference = db.collection("friend_request");
                friendReference.add(new FriendRequest(userId, friendId, new Date()));
            }
        });
    }

    public static void rejectRequest(FriendRequest request) {
        rejectRequest(request.getSenderId(), request.getReceiverId());
    }

    public static void rejectRequest(String userId, String friendId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendReference = db.collection("friend_request");
        friendReference.where(and(equalTo("receiverId", friendId), equalTo("senderId", userId))).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
            }
        });
    }

    public static void checkRequestExists(String senderId, String receiverId, OnRequestExistListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendReference = db.collection("friend_request");

        friendReference.whereEqualTo("senderId", senderId).whereEqualTo("receiverId", receiverId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = !task.getResult().isEmpty();
                listener.onRequestExistChecked(exists);
            }
        });
    }

    public static void removeFriend(String friendId) {
        UserService.getUser(user -> {
            user.removeFriend(friendId);
            UserService.updateUser(user);
        });

        //add friend to friend
        UserService.getUserById(friendId, user -> {
            user.removeFriend(FirebaseAuth.getInstance().getUid());
            UserService.updateUser(user);
        });

    }

    public static void getFriend(User user, OnFriendLoadedListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereNotIn("friends", user.getFriends())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    assert value != null;
                    List<User> users = value.toObjects(User.class);
                    users.remove(user);
                    listener.onFriendLoaded(users);
                });

    }

    public interface OnFriendLoadedListener {
        void onFriendLoaded(List<User> users);
    }

    public interface OnRequestExistListener {
        void onRequestExistChecked(boolean exists);
    }


    public interface OnRequestLoadedListener {
        void onRequestLoaded(List<FriendRequest> requests);
    }
}
