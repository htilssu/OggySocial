package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.FriendRequest;
import com.oggysocial.oggysocial.models.Notify;
import com.oggysocial.oggysocial.services.FriendService;
import com.oggysocial.oggysocial.services.UserService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    List<? extends Notify> notifyList;

    public NotifyAdapter(List<? extends Notify> notifyList) {
        this.notifyList = notifyList;
    }


    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new NotifyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        FriendRequest friendRequest = (FriendRequest) notifyList.get(position);
        UserService.getUserById(friendRequest.getSenderId(), user -> {
            holder.title.setText(user.getFullName() + " " + holder.itemView.getResources().getString(R.string.sent_request_to_you));
            if (user.getAvatar() != null) {
                Glide.with(holder.itemView).load(user.getAvatar()).into(holder.civAvatar);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (notifyList != null) {
            return notifyList.size();
        }
        return 0;
    }

    public void setNotifyList(List<? extends Notify> notifyList) {
        this.notifyList = notifyList;
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        MaterialButton btnAccept, btnReject;
        CircleImageView civAvatar;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews();
            initListener();
        }

        private void initViews() {
            title = itemView.findViewById(R.id.title);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            civAvatar = itemView.findViewById(R.id.avatar);
        }

        private void initListener() {
            btnAccept.setOnClickListener(v -> {
                FriendRequest request = (FriendRequest) notifyList.get(getAbsoluteAdapterPosition());
                FriendService.acceptRequest(request);
                notifyList.remove(getAbsoluteAdapterPosition());
                notifyItemRemoved(getAbsoluteAdapterPosition());
            });

            btnReject.setOnClickListener(v -> {
                FriendRequest request = (FriendRequest) notifyList.get(getAbsoluteAdapterPosition());
                FriendService.rejectRequest(request);
                notifyList.remove(getAbsoluteAdapterPosition());
                notifyItemRemoved(getAbsoluteAdapterPosition());
            });
        }
    }
}
