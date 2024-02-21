package com.oggysocial.oggysocial.adapters;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Post;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    ArrayList<Post> posts;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.tvAuthorName.setText(posts.get(position).getAuthor());
        holder.tvPostContent.setText(posts.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvPostContent, tvLikeCount;
        CircleImageView ivAuthorAvatar;
        Button btnLike;
        boolean isLiked = false;
        View v;

        public PostHolder(@NonNull View view) {
            super(view);
            v = view;
            tvAuthorName = view.findViewById(R.id.tvAuthorName);
            tvLikeCount = view.findViewById(R.id.tvLikeCount);
            tvPostContent = view.findViewById(R.id.tvPostContent);
            ivAuthorAvatar = view.findViewById(R.id.ivAuthorImage);
            btnLike = view.findViewById(R.id.btnLike);
            changeLikeStatus(isLiked);
            initListener();
        }

        private void initListener() {
            btnLike.setOnClickListener(v -> {
                isLiked = !isLiked;
                changeLikeStatus(isLiked);
            });
        }

        private void changeLikeStatus(boolean isLiked) {
            int iconSize = (int) (24 * v.getResources().getDisplayMetrics().density);
            if (!isLiked) {
                btnLike.setTextColor(v.getResources().getColor(R.color.inactive, null));
                Drawable drawable = ResourcesCompat.getDrawable(v.getContext().getResources(), R.drawable.heart_line, null);
                assert drawable != null;
                drawable.setBounds(0, 0, iconSize, iconSize);
                btnLike.setCompoundDrawables(drawable, null, null, null);
            } else {
                btnLike.setTextColor(v.getResources().getColor(R.color.colorPrimary, null));
                Drawable drawable = ResourcesCompat.getDrawable(v.getContext().getResources(), R.drawable.heart_solid, null);
                assert drawable != null;
                drawable.setColorFilter(v.getResources().getColor(R.color.red_heart, null), PorterDuff.Mode.SRC_IN);
                drawable.setBounds(0, 0, iconSize, iconSize);
                btnLike.setCompoundDrawables(drawable, null, null, null);
            }
        }

    }
}
