package com.oggysocial.oggysocial.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.PopupActivity;
import com.oggysocial.oggysocial.fragments.main.UpdatePostFragment;
import com.oggysocial.oggysocial.services.PostService;

public class PostBottomSheetModel extends BottomSheetDialog {
    Post post;
    OnDeletePost onDeletePost;
    OnUpdatedPost onupdatedPost;

    public PostBottomSheetModel(@NonNull Context context, Post post) {
        super(context);
        this.post = post;
    }

    public void setOnUpdatedPost(OnUpdatedPost onupdatedPost) {
        this.onupdatedPost = onupdatedPost;
    }

    public void setOnDeletePost(OnDeletePost onDeletePost) {
        this.onDeletePost = onDeletePost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(View.inflate(getContext(), R.layout.post_bottom_sheet, null));
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = getBehavior();
        bottomSheetBehavior.setPeekHeight(500);

        initViews();
    }


    private void initViews() {

        initListeners();
    }

    private void initListeners() {
        LinearLayout btnDelete = findViewById(R.id.llDeletePost);
        LinearLayout btnEdit = findViewById(R.id.llEditPost);
        assert btnDelete != null;
        btnDelete.setOnClickListener(v -> {
            PostService.deletePost(post);
            onDeletePost.onDelete();
            dismiss();
        });

        for (int i = 0; i < btnDelete.getChildCount(); i++) {
            View child = btnDelete.getChildAt(i);
            child.setOnClickListener(v -> btnDelete.performClick());
        }

        assert btnEdit != null;
        btnEdit.setOnClickListener(l -> {
            dismiss();
            Intent intent = new Intent(getContext(), PopupActivity.class);
            intent.putExtra("popup", Popup.UPDATE_POST);
            intent.putExtra("post", post);
            getContext().startActivity(intent);
            UpdatePostFragment.setOnUpdatedPost(() -> onupdatedPost.onUpdated());
        });
        for (int i = 0; i < btnEdit.getChildCount(); i++) {
            View child = btnEdit.getChildAt(i);
            child.setOnClickListener(v -> btnEdit.performClick());
        }

    }

    public interface OnDeletePost {
        void onDelete();
    }

    public interface OnUpdatedPost {
        void onUpdated();
    }
}
