package com.oggysocial.oggysocial.services;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.oggysocial.oggysocial.models.Comment;
import com.oggysocial.oggysocial.models.Post;

import java.util.List;
import java.util.function.Consumer;

public class CommentService {

    public static void addComment(Comment comment) {
        new Thread(() -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("comments").document(comment.getId()).set(comment);
        }).start();
    }

    public static void deleteComment(String commentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comments").document(commentId).delete();
    }

    public static void updateComment(String commentId, Comment comment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comments").document(commentId).set(comment);
    }

    public static void getPostComment(Post post, Consumer<List<Comment>> consumer) {
        if (post.getComments().isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("comments").whereEqualTo("postId", post.getId()).orderBy("date", Query.Direction.DESCENDING)
                    .addSnapshotListener((task, ex) -> {
                        if (ex != null) return;
                        post.getComments().clear();
                        if (task == null) return;
                        List<Comment> commentList = task.toObjects(Comment.class);
                        post.setComments(commentList);
                        consumer.accept(commentList);
                    });
        }
    }

}
