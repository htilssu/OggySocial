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
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.PostService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    List<Post> posts;

    public PostAdapter(List<Post> posts) {
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

        String fullName = "123";
        posts.get(position).getImages().forEach((s, uri) -> {
            Glide.with(holder.itemView.getContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivPostImage);
        });
        holder.tvAuthorName.setText(fullName);
        holder.tvPostContent.setText(posts.get(position).getContent());
        holder.tvLikeCount.setText(String.valueOf(posts.get(position).getLikes().size()));
        String commentCount = posts.get(position).getComments().size() + " " + holder.itemView.getResources().getString(R.string.comment);
        holder.tvCommentCount.setText(commentCount);
        boolean isLiked = posts.get(position).getLikes().contains(FirebaseAuth.getInstance().getUid());
        holder.setLiked(isLiked);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvPostContent, tvLikeCount, tvCommentCount;
        CircleImageView ivAuthorAvatar;
        ImageView ivPostImage;
        Button btnLike;
        boolean isLiked = false;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            ivAuthorAvatar = itemView.findViewById(R.id.ivAuthorImage);
            btnLike = itemView.findViewById(R.id.btnLike);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
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
                posts.get(getBindingAdapterPosition()).getLikes().remove(FirebaseAuth.getInstance().getUid());
                addLike(-1);
                PostService.updatePost(posts.get(getBindingAdapterPosition()));
            }
        }

        private void initListener() {
            btnLike.setOnClickListener(v -> {
                setLiked(!isLiked);
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
            } else {
                btnLike.setTextColor(itemView.getResources().getColor(R.color.colorPrimary, null));
                Drawable drawable = ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.heart_solid, null);
                assert drawable != null;
                drawable.setColorFilter(itemView.getResources().getColor(R.color.red_heart, null), PorterDuff.Mode.SRC_IN);
                drawable.setBounds(0, 0, iconSize, iconSize);
                btnLike.setCompoundDrawables(drawable, null, null, null);
            }
        }

    }
}
