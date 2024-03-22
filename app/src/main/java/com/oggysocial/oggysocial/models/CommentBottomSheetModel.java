package com.oggysocial.oggysocial.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.CommentAdapter;
import com.oggysocial.oggysocial.adapters.CommentTouchHelper;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.time.LocalDateTime;
import java.util.Date;

public class CommentBottomSheetModel extends BottomSheetDialog {

    Post post;
    RecyclerView rvComment;
    EditText etComment;
    ImageView ivSend;
    CommentAdapter commentAdapter;

    public CommentBottomSheetModel(@NonNull Context context, Post post) {
        super(context);
        this.post = post;
        commentAdapter = new CommentAdapter(post.getComments());
        commentAdapter.setOnDeletedCommentListener(i -> PostService.updatePost(this.post));

        BottomSheetBehavior<FrameLayout> behavior = getBehavior();
        setContentView(R.layout.comment_bottom_sheet);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        behavior.setHalfExpandedRatio(0.5f);

        initView();

        loadData();
        initListener();
    }

    private void initListener() {
        ivSend.setOnClickListener(v -> {
            String commentContent = etComment.getText().toString();
            if (!commentContent.isEmpty()) {
                Comment comment = new Comment();
                comment.setContent(commentContent);
                comment.setAuthor(UserService.user);
                comment.setDate(new Date());
                post.addComment(comment);
                PostService.updatePost(post);
                etComment.setText("");
            }

        });
    }

    private void initView() {
        rvComment = findViewById(R.id.rvComment);
        assert rvComment != null;
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentTouchHelper commentTouchHelper = new CommentTouchHelper(0, ItemTouchHelper.LEFT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(commentTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvComment);
        rvComment.setAdapter(commentAdapter);
        etComment = findViewById(R.id.etComment);
        ivSend = findViewById(R.id.ivSendComment);
        ConstraintLayout clComment = findViewById(R.id.bottomSheetComment);
        assert clComment != null;
        clComment.setMinHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {

        PostService.getPostRealTime(post.getId(), post -> {
            this.post = post;
            if (commentAdapter != null) {
                commentAdapter.setComments(post.getComments());
                commentAdapter.notifyDataSetChanged();
            }
        });
    }


}
