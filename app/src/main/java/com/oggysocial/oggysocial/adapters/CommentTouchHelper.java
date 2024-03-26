package com.oggysocial.oggysocial.adapters;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Comment;

import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CommentTouchHelper extends ItemTouchHelper.SimpleCallback {

    public CommentTouchHelper(int dragDirs, int swipeDirs, CommentAdapter commentAdapter) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.bg_delete))
                .addSwipeLeftActionIcon(R.drawable.trash_line)
                .setSwipeLeftActionIconTint(R.color.white)
                .create()
                .decorate();


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        CommentAdapter adapter = (CommentAdapter) viewHolder.getBindingAdapter();
        assert adapter != null;
        Comment comment = adapter.comments.get(viewHolder.getAbsoluteAdapterPosition());

        if (comment.getAuthor() == null) {
            return 0;
        }

        if (!comment.getAuthor().getId().equals(FirebaseAuth.getInstance().getUid())) {
            return 0;
        }

        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        CommentAdapter adapter = (CommentAdapter) viewHolder.getBindingAdapter();
        assert adapter != null;
        Comment comment = adapter.comments.get(viewHolder.getAbsoluteAdapterPosition());
        if (comment.getAuthor() == null) {
            return;
        }
        if (direction == ItemTouchHelper.LEFT && Objects.equals(comment.getAuthor().getId(), FirebaseAuth.getInstance().getUid())) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            adapter.deleteComment(position);
        }
    }
}