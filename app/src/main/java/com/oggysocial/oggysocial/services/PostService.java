package com.oggysocial.oggysocial.services;

import static com.google.firebase.firestore.Filter.equalTo;
import static com.google.firebase.firestore.Filter.or;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.oggysocial.oggysocial.models.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PostService {
    static FirebaseStorage storage = FirebaseDB.getStorage();
    static Source source = Source.CACHE;


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
        newPost.setDate(new Date());
        String postId = UUID.randomUUID().toString();
        newPost.setId(postId);
        db.collection("posts").document(postId).set(newPost).addOnSuccessListener(command -> {
            UserService.getUser(user -> {
                user.addPost(newPost.getId());
                newPost.setUser(user);
                listener.onPostSaved(newPost);
                UserService.updateUser(user);
                updatePost(newPost);
            });
        });
    }

    public static void getPost(String postId, OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).get().addOnSuccessListener(documentSnapshot -> listener.onPostLoaded(documentSnapshot.toObject(Post.class)));
    }

    public static void getPostRealTime(String postId, OnPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                listener.onPostLoaded(documentSnapshot.toObject(Post.class));
            }
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
            db.collection("posts").document(postId).delete().addOnSuccessListener(command -> UserService.getUser(user -> {
                user.removePost(postId);
                UserService.updateUser(user);
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
            UserService.updateUser(user);
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

        query.get().addOnSuccessListener(snapshot -> {
            List<Post> posts = snapshot.toObjects(Post.class);
            listener.onListPostLoaded(posts);
        });

        query.addSnapshotListener((command, e) -> {

            if (command != null) {
                List<Post> posts = command.toObjects(Post.class);
                listener.onListPostLoaded(posts);
            }
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

    public static void getAllPost(OnListPostLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            assert value != null;
            List<Post> posts = value.toObjects(Post.class);
            listener.onListPostLoaded(posts);
        });
    }

    /**
     * Lấy thông tin bài viết mới của bạn bè
     *
     * @param listener listener
     */
    public static void getNewFeeds(OnListPostLoadedListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        UserService.getUser(user -> {
            List<String> friendList = new ArrayList<>(user.getFriends());
            friendList.add(user.getId());
            db.collection("posts").whereIn("author", friendList).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                if (error != null) {
                    return;
                }
                assert value != null;
                List<Post> posts = value.toObjects(Post.class);
                listener.onListPostLoaded(posts);
            });
        });

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
