package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.FriendService;
import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.userViewHolder>{
    private List<User> userList;
    private OnItemClickListener listener;

    // Interface định nghĩa phương thức xử lý sự kiện click xem profile
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public AddFriendAdapter(List<User> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addfriend, parent, false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendAdapter.userViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarFriend;
        private TextView tvNameFriend;
        private Button btnAddFriend;
        boolean flag = false;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarFriend = itemView.findViewById(R.id.avatarFriend);
            tvNameFriend = itemView.findViewById(R.id.tvNameFriend);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);

            // Xử lý sự kiện click trên itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Kiểm tra xem listener có tồn tại không trước khi gọi phương thức onItemClick
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            
            // Trong AddFriendAdapter, trong phương thức onClick của nút "Kết Bạn"
            btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = userList.get(position);
                        if (flag)
                        {
                            FriendService.rejectRequest(FirebaseAuth.getInstance().getUid(), user.getId());
                            btnAddFriend.setText("Thêm bạn bè");
                        }
                        else {
                            FriendService.sendRequest(FirebaseAuth.getInstance().getUid(), user.getId());
                            btnAddFriend.setText("Hủy lời mời");
                        }
                    }
                }
            });
        }

        public void bind(User user) {
            // Set dữ liệu của friend thành phần giao diện
            if (user.getAvatar() != null)
            {
                Glide.with(itemView).load(user.getAvatar()).into(avatarFriend);
            }
            tvNameFriend.setText(user.getFullName());
            FriendService.checkRequestExists(FirebaseAuth.getInstance().getUid(), user.getId(),
                    exists ->
                {
                if (!exists)
                    {
                        btnAddFriend.setText("Thêm bạn bè");
                        flag = false;
                    }
                else {
                    btnAddFriend.setText("Hủy lời mời");
                    flag = true;
                }
                });
        }
    }
}
