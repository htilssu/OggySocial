package com.oggysocial.oggysocial.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.CommentBottomSheetModel;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.PostBottomSheetModel;
import com.oggysocial.oggysocial.services.PostService;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
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

        Post currentPost = posts.get(position);

        if (!Objects.equals(currentPost.getAuthor(), FirebaseAuth.getInstance().getUid())) {
            holder.btnPostMenu.setVisibility(View.GONE);
        }

        if (!currentPost.getImages().isEmpty()) {
            holder.ivPostImage.setVisibility(View.VISIBLE);
            currentPost.getImages().forEach((s, s2) -> {
                Glide.with(holder.itemView.getContext()).load(s2).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivPostImage);
            });
        } else {
            holder.ivPostImage.setVisibility(View.GONE);
        }

        holder.tvPostContent.setText(posts.get(position).getContent());
        holder.tvLikeCount.setText(String.valueOf(posts.get(position).getLikes().size()));
        String commentCount = posts.get(position).getComments().size() + " " + holder.itemView.getResources().getString(R.string.comment);
        holder.tvCommentCount.setText(commentCount);

        if (currentPost.getUser() != null) {
            holder.tvAuthorName.setText(currentPost.getUser().getFullName());
            if (currentPost.getUser().getAvatar() != null) {
                Glide.with(holder.itemView.getContext()).load(currentPost.getUser().getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivAuthorAvatar);
            } else {
                Glide.with(holder.itemView.getContext()).load(R.drawable.default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivAuthorAvatar);
            }
        }

        boolean isLiked = posts.get(position).getLikes().contains(FirebaseAuth.getInstance().getUid());
        holder.setLiked(isLiked);
    }

    @Override
    public int getItemCount() {
        if (posts != null) return posts.size();
        return 0;
    }


    public class PostHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvPostContent, tvLikeCount, tvCommentCount;
        CircleImageView ivAuthorAvatar;
        ImageView ivPostImage, btnPostMenu;
        MaterialButton btnLike, btnComment;
        boolean isLiked = false;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            initView();
            initListener();
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
            changeLikeStatus(isLiked);
            if (isLiked) {
                if (!posts.get(getBindingAdapterPosition()).getLikes().contains(FirebaseAuth.getInstance().getUid())) {
                    posts.get(getBindingAdapterPosition()).getLikes().add(FirebaseAuth.getInstance().getUid());
                    addLike(1);
                    PostService.updatePost(posts.get(getBindingAdapterPosition()));
                }
            } else {
                if (posts.get(getBindingAdapterPosition()).getLikes().contains(FirebaseAuth.getInstance().getUid())) {
                    posts.get(getBindingAdapterPosition()).getLikes().remove(FirebaseAuth.getInstance().getUid());
                    addLike(-1);
                    PostService.updatePost(posts.get(getBindingAdapterPosition()));
                }
            }
        }

        private void initView() {
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            ivAuthorAvatar = itemView.findViewById(R.id.ivAuthorImage);
            btnLike = itemView.findViewById(R.id.btnLike);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            btnPostMenu = itemView.findViewById(R.id.btnPostMenu);
            btnComment = itemView.findViewById(R.id.btnComment);
        }

        private void initListener() {
            btnLike.setOnClickListener(v -> setLiked(!isLiked));
            btnPostMenu.setOnClickListener(v -> {
                PostBottomSheetModel postBottomSheetModel = new PostBottomSheetModel(this.itemView.getContext(), posts.get(getBindingAdapterPosition()));
                postBottomSheetModel.show();
                postBottomSheetModel.setOnDeletePost(() -> {
                    posts.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                });
                postBottomSheetModel.setOnUpdatedPost(() -> notifyItemChanged(getBindingAdapterPosition()));
            });

            btnComment.setOnClickListener(v -> {
                CommentBottomSheetModel commentBottomSheetModel = new CommentBottomSheetModel(this.itemView.getContext(), posts.get(getBindingAdapterPosition()));
                commentBottomSheetModel.show();
//                commentBottomSheetModel.setOnUpdatedPost(() -> notifyItemChanged(getBindingAdapterPosition()));
            });
        }

        private void addLike(int number) {
            int currentLike = Integer.parseInt(tvLikeCount.getText().toString());
            tvLikeCount.setText(String.valueOf(currentLike + number));
        }

        public void changeLikeStatus(boolean isLiked) {
            int iconSize = (int) (24 * itemView.getResources().getDisplayMetrics().density);
            if (!isLiked) {
                btnLike.setTextColor(itemView.getResources().getColor(R.color.inactive, null));
                Drawable drawable = ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.heart_line, null);
                assert drawable != null;
                drawable.setBounds(0, 0, iconSize, iconSize);
                btnLike.setCompoundDrawables(drawable, null, null, null);
                btnLike.setIcon(drawable);

            } else {
                btnLike.setTextColor(itemView.getResources().getColor(R.color.colorPrimary, null));
                Drawable drawable = ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.heart_solid, null);
                assert drawable != null;
                drawable.setColorFilter(itemView.getResources().getColor(R.color.red_heart, null), PorterDuff.Mode.SRC_IN);
                drawable.setBounds(0, 0, iconSize, iconSize);
                btnLike.setCompoundDrawables(drawable, null, null, null);
                btnLike.setIcon(drawable);
            }
        }

    }
}
