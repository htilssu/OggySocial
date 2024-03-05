package com.oggysocial.oggysocial.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PostService {
    static FirebaseStorage storage = FirebaseDB.getStorage();

    public static List<Post> getPosts(String userId) {
        return null;
    }

    /**
     * Lưu một bài viết
     *
     * @param newPost bài viết mới
     */
    public static void savePost(Post newPost) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        newPost.setAuthor(FirebaseAuth.getInstance().getUid());
        newPost.setDate(LocalDateTime.now().toString());
        db.collection("posts")
                .add(newPost)
                .addOnSuccessListener(command -> {
                    newPost.setId(command.getId());
                    updatePost(newPost);
                    UserService.getUser(user -> {
                        user.addPost(newPost.getId());
                        UserService.saveUser(user);
                    });
                });
    }

    public static void getPost(String postId, OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    listener.onPostLoaded(documentSnapshot.toObject(Post.class));
                });
    }

    /**
     * Xóa một bài viết
     *
     * @param postId id của bài viết
     */
    public static void deletePost(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getPost(postId, post -> {
            db.collection("posts").document(postId).delete().addOnSuccessListener(command -> {
                UserService.getUser(user -> {
                    user.removePost(postId);
                    UserService.saveUser(user);
                });
            });
            Map<String, String> images = post.getImages();
            if (images != null) {
                images.forEach((s, uri) -> {
                    storage.getReferenceFromUrl(uri).delete();
                });
            }
        });
    }

    public static void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getId()).delete().addOnSuccessListener(command -> {
            UserService.getUser(user -> {
                user.removePost(post.getId());
                UserService.saveUser(user);
            });
        });
        Map<String, String> images = post.getImages();
        if (images != null) {
            images.forEach((s, uri) -> {
                storage.getReferenceFromUrl(uri).delete();
            });
        }
    }

    public static void updatePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getId()).set(post);
    }


    /**
     * Lấy thông tin bài viết của user
     *
     * @param userId   id của user
     * @param listener listener
     */
    public static void getUserPosts(String userId, OnListPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").whereEqualTo("author", userId).limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listener.onListPostLoaded(queryDocumentSnapshots.toObjects(Post.class));
                });

    }

    /**
     * Lấy thông tin bài viết của user hiện tại
     *
     * @param listener listener
     */
    public static void getUserPosts(OnListPostLoadedListener listener) {
        getUserPosts(FirebaseAuth.getInstance().getUid(), listener);
    }

    /**
     * Lấy thông tin bài viết mới của bạn bè
     *
     * @param listener listener
     */
    public static void getNewFeeds(OnListPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listener.onListPostLoaded(queryDocumentSnapshots.toObjects(Post.class));
                });

    }

    public interface OnListPostLoadedListener {
        void onListPostLoaded(List<Post> posts);
    }

    public interface OnPostLoadedListener {
        void onPostLoaded(Post post);
    }
}
