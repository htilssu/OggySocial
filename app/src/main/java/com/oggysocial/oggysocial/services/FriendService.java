package com.oggysocial.oggysocial.services;

import static com.google.firebase.firestore.Filter.and;
import static com.google.firebase.firestore.Filter.equalTo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oggysocial.oggysocial.models.FriendRequest;

import java.util.Date;
import java.util.List;


public class FriendService {


    public static void getRequest(OnRequestLoadedListener listener) {
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendReference = db.collection("friend_request");
        friendReference.where(and(equalTo("receiverId", request.getReceiverId()), equalTo("senderId", request.getSenderId()))).get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
        });


        addFriend(request.getSenderId());
    }

    public static void rejectRequest(FriendRequest request) {
        rejectRequest(request.getSenderId(), request.getReceiverId());
    }

    public static void rejectRequest(String userId, String friendId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendReference = db.collection("friend_request");
        friendReference.where(and(equalTo("receiverId", friendId), equalTo("senderId", userId))).get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
        });
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendReference = db.collection("friend_request");
        friendReference.add(new FriendRequest(userId, friendId, new Date()));
    }

    public static void removeFriend(String friendId) {
        UserService.getUser(user -> {
            user.removeFriend(friendId);
            UserService.saveUser(user);
        });

        //add friend to friend
        UserService.getUserById(friendId, user -> {
            user.removeFriend(FirebaseAuth.getInstance().getUid());
            UserService.saveUser(user);
        });

    }

    public interface OnRequestLoadedListener {
        void onRequestLoaded(List<FriendRequest> requests);
    }
}
