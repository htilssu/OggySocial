package com.oggysocial.oggysocial.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.User;

import java.time.LocalDateTime;
import java.util.List;
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
        db.collection("posts").add(newPost);
    }

    /**
     * Xóa một bài viết
     *
     * @param postId id của bài viết
     */
    public static void deletePost(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).delete();
    }

    public static void likePost(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    /**
     * Lấy thông tin bài viết theo id
     *
     * @param postId id của bài viết
     * @return {@link Post} bài viết
     */
    public static Post getPostById(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AtomicReference<Post> post = new AtomicReference<>();
        db.collection("posts").document(postId)
                .get()
                .addOnSuccessListener(command -> {
                    post.set(command.toObject(Post.class));
                });
        User user = UserService.getUserById(post.get().getAuthor());
        return post.get();
    }

    /**
     * Lấy thông tin bài viết của user
     *
     * @param userId   id của user
     * @param listener listener
     */
    public static void getUserPosts(String userId, OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").whereEqualTo("author", userId).limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listener.onPostLoaded(queryDocumentSnapshots.toObjects(Post.class));
                });

    }

    /**
     * Lấy thông tin bài viết của user hiện tại
     *
     * @param listener listener
     */
    public static void getUserPosts(OnPostLoadedListener listener) {
        getUserPosts(FirebaseAuth.getInstance().getUid(), listener);
    }

    /**
     * Lấy thông tin bài viết mới của bạn bè
     *
     * @param listener listener
     */
    public static void getNewFeeds(OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listener.onPostLoaded(queryDocumentSnapshots.toObjects(Post.class));
                });

    }

    public interface OnPostLoadedListener {
        void onPostLoaded(List<Post> posts);
    }
}
