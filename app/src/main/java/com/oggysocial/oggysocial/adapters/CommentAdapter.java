package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Comment;
import com.oggysocial.oggysocial.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        User author = comment.getAuthor();
        if (author != null) {
            holder.tvCommentAuthor.setText(author.getFullName());
            Glide.with(holder.itemView).load(author.getAvatar()).into(holder.civAvatar);
        } else {
            holder.tvCommentAuthor.setText("...");
            holder.civAvatar.setImageResource(R.drawable.default_avatar);
        }
        holder.tvCommentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        TextView tvCommentContent, tvCommentAuthor;
        CircleImageView civAvatar;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
            civAvatar = itemView.findViewById(R.id.civAvatar);
            tvCommentAuthor = itemView.findViewById(R.id.tvCommentAuthor);
        }
    }
}