package com.oggysocial.oggysocial.services;

import static com.google.firebase.firestore.Filter.equalTo;
import static com.google.firebase.firestore.Filter.or;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.oggysocial.oggysocial.models.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostService {
    private static final String USA = "USA";
    static FirebaseStorage storage = FirebaseDB.getStorage();

    public static List<Post> getPosts(String userId) {
        return null;
    }

    /**
     * Lưu một bài viết
     *
     * @param newPost bài viết mới
     */
    public static void savePost(Post newPost, OnPostSavedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        newPost.setAuthor(FirebaseAuth.getInstance().getUid());
        newPost.setDate(LocalDateTime.now().toString());
        db.collection("posts").add(newPost).addOnSuccessListener(command -> {
            newPost.setId(command.getId());
            updatePost(newPost);
            UserService.getUser(user -> {
                user.addPost(newPost.getId());
                newPost.setUser(user);
                listener.onPostSaved(newPost);
                UserService.saveUser(user);
            });
        });
    }

    public static void getPost(String postId, OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).get().addOnSuccessListener(documentSnapshot -> listener.onPostLoaded(documentSnapshot.toObject(Post.class)));
    }

    /**
     * Xóa một bài viết
     *
     * @param postId id của bài viết
     */
    public static void deletePost(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getPost(postId, post -> {
            db.collection("posts").document(postId).delete().addOnSuccessListener(command -> UserService.getUser(user -> {
                user.removePost(postId);
                UserService.saveUser(user);
            }));
            Map<String, String> images = post.getImages();
            if (images != null) {
                images.forEach((s, uri) -> storage.getReferenceFromUrl(uri).delete());
            }
        });
    }

    public static void deletePost(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getId()).delete().addOnSuccessListener(command -> UserService.getUser(user -> {
            user.removePost(post.getId());
            UserService.saveUser(user);
        }));
        Map<String, String> images = post.getImages();
        if (images != null) {
            images.forEach((s, uri) -> storage.getReferenceFromUrl(uri).delete());
        }
    }

    /**
     * Cập nhật thông tin bài viết
     *
     * @param post bài viết
     */
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

        Query query = db.collection("posts").where(or(equalTo("author", userId))).orderBy("date", Query.Direction.DESCENDING);
        query.addSnapshotListener((command, e) -> {
            assert command != null;
            List<Post> posts = command.toObjects(Post.class);
            UserService.getUser(user -> posts.forEach(post -> post.setUser(user)));
            listener.onListPostLoaded(posts);

        });

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Post> posts = queryDocumentSnapshots.toObjects(Post.class);
            UserService.getUser(user -> posts.forEach(post -> post.setUser(user)));
            listener.onListPostLoaded(posts);
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
        db.collection("posts").limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> listener.onListPostLoaded(queryDocumentSnapshots.toObjects(Post.class)));

    }

    public interface OnListPostLoadedListener {
        void onListPostLoaded(List<Post> posts);
    }

    public interface OnPostLoadedListener {
        void onPostLoaded(Post post);
    }

    public interface OnPostSavedListener {
        void onPostSaved(Post post);
    }
}
