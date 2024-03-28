package com.oggysocial.oggysocial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.models.User;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    FriendLayoutType layoutType;
    private List<User> friends;

    public FriendAdapter(List<User> friends, FriendLayoutType layoutType) {
        this.friends = friends;
        this.layoutType = layoutType;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setLayoutType(FriendLayoutType layoutType) {
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutType == FriendLayoutType.GRID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_grid_card, parent, false);
            return new FriendViewHolder(view);
        }
//        return new FriendViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.friendName.setText(friend.getFullName());
        if (friend.getAvatar() != null) {
            Glide.with(holder.itemView).load(friend.getAvatar()).into(holder.friendImage);
        }
    }

    @Override
    public int getItemCount() {
        if (friends == null) return 0;
        return Math.min(friends.size(), 6);
    }

    public enum FriendLayoutType {
        GRID, LIST
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView friendImage;
        final TextView friendName;

        public FriendViewHolder(View view) {
            super(view);
            friendImage = view.findViewById(R.id.ivAvatar);
            friendName = view.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            User friend = friends.get(getAbsoluteAdapterPosition());
            FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new ProfileFragment(friend, true))
                    .addToBackStack(null).commit();
        }
    }
}