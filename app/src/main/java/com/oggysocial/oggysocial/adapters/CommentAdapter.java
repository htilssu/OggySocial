package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Comment;
import com.oggysocial.oggysocial.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    List<Comment> comments;
    OnCreatedCommentListener onCreatedCommentListener;
    OnUserAvatarClickListener onUserAvatarClickListener;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setOnCreatedCommentListener(OnCreatedCommentListener onCreatedCommentListener) {
        this.onCreatedCommentListener = onCreatedCommentListener;
    }

    public void setOnUserAvatarClickListener(OnUserAvatarClickListener onUserAvatarClickListener) {
        this.onUserAvatarClickListener = onUserAvatarClickListener;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        return 0;
    }

    public interface OnCreatedCommentListener {
        void onCreatedComment(Comment comment);
    }

    public interface OnUserAvatarClickListener {
        void onUserAvatarClick(User user);
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        TextView tvCommentContent;
        CircleImageView civAvatar;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            initView();
            initListener();
        }

        private void initView() {
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
            civAvatar = itemView.findViewById(R.id.civAvatar);
        }

        private void initListener() {
            civAvatar.setOnClickListener(v -> {
                if (onUserAvatarClickListener != null) {
                    onUserAvatarClickListener.onUserAvatarClick(comments.get(getAbsoluteAdapterPosition()).getAuthor());
                }
            });
        }
    }
}
