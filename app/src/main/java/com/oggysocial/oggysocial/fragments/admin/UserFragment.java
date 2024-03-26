package com.oggysocial.oggysocial.fragments.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.UserAdapter;
import com.oggysocial.oggysocial.adapters.UserAdapterType;
import com.oggysocial.oggysocial.fragments.main.ProfileFragment;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.UserService;

public class UserFragment extends Fragment {
    RecyclerView recyclerView;
    private UserAdapter userAdapter;

    public UserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.content_recycle_view, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(null, UserAdapterType.LIST_ADMIN);
        userAdapter.setOnUserClickListener(user -> {
            ProfileFragment profileFragment = new ProfileFragment(user, false);
            getParentFragmentManager().beginTransaction().replace(R.id.user_content, profileFragment).addToBackStack(null).commit();
        });
        recyclerView.setAdapter(userAdapter);

        loadUsers();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadUsers() {
        new Thread(() -> {
            UserService.getAllUser(users -> {
                users.removeIf(user -> user.getBlocked() || user.getRole().equals("admin"));
                userAdapter.setUserList(users);
                new Handler(Looper.getMainLooper()).post(() -> userAdapter.notifyDataSetChanged());
            });
        }).start();
    }
}
