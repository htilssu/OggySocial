package com.oggysocial.oggysocial.models;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.services.PostService;

public class PostBottomSheetModel extends BottomSheetDialog {
    Post post;

    public PostBottomSheetModel(@NonNull Context context, Post post) {
        super(context);
        this.post = post;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_bottom_sheet);
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = getBehavior();
        bottomSheetBehavior.setPeekHeight(300);
        LinearLayout btnDelete = findViewById(R.id.btnDeletePost);
        assert btnDelete != null;
        btnDelete.setOnClickListener(v -> {
            PostService.deletePost(post);
            dismiss();
        });

        for (int i = 0; i < btnDelete.getChildCount(); i++) {
            View child = btnDelete.getChildAt(i);
            child.setOnClickListener(v -> {
                btnDelete.performClick();
            });
        }

    }
}
