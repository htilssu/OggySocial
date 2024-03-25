package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.UserService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    List<User> userList;
    OnUserClickListener onUserClickListener;
    UserAdapterType userAdapterType;

    public UserAdapter(List<User> userList, UserAdapterType userAdapterType) {
        this.userAdapterType = userAdapterType;
        this.userList = userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (userAdapterType) {
            case SEARCH ->
                    new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false));
            case LIST_ADMIN ->
                    new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false));
        };
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getFullName());

        //TODO handle avatar
        if (user.getAvatar() != null) {
            Glide.with(holder.itemView).load(user.getAvatar()).into(holder.civAvatar);
        }
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        TextView tvUsername;
        ImageView ivBlock;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            civAvatar = itemView.findViewById(R.id.civAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            if (userAdapterType == UserAdapterType.LIST_ADMIN) {
                ivBlock = itemView.findViewById(R.id.ivBlockUser);
            }
            initListeners();
        }

        private void initListeners() {
            itemView.setOnClickListener(v -> {
                onUserClickListener.onUserClick(userList.get(getBindingAdapterPosition()));
            });
            if (userAdapterType == UserAdapterType.LIST_ADMIN) {
                ivBlock.setOnClickListener(v -> {

                    User user = userList.get(getBindingAdapterPosition());
                    AlertDialog.Builder builder;
                    if (!user.getBlocked()) {
                        builder = getBuilder("Bạn có chắc chắn muốn chặn người dùng này không?", user, true);

                    } else {
                        builder = getBuilder("Bạn có chắc chắn muốn hủy chặn người dùng này không?", user, false);

                    }
                    builder.create().show();

                });

            }
        }

        @NonNull
        private AlertDialog.Builder getBuilder(String message, User user, boolean blocked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            builder.setTitle("Xác nhận");
            builder.setMessage(message);

            builder.setPositiveButton("Có", (dialog, which) -> {
                user.setBlocked(blocked);
                userList.remove(user);
                notifyItemRemoved(getBindingAdapterPosition());
                UserService.updateUser(user);
            });
            builder.setNegativeButton("Không", (dialog, which) -> {
                dialog.dismiss();
            });
            return builder;
        }
    }
}
