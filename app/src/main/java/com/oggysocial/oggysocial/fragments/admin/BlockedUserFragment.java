package com.oggysocial.oggysocial.fragments.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.oggysocial.oggysocial.services.UserService;

public class BlockedUserFragment extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;

    public BlockedUserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.content_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        recyclerView = requireView().findViewById(R.id.recyclerView);
        userAdapter = new UserAdapter(null, UserAdapterType.LIST_ADMIN);
        userAdapter.setOnUserClickListener(user -> {

            getParentFragmentManager().beginTransaction().replace(R.id.user_content, new ProfileFragment(user, false)).addToBackStack(null).commit();
        });
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadBlockedUsers();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadBlockedUsers() {
        UserService.getAllUser(userList -> {
            userList.removeIf(user -> !user.getBlocked());
            requireActivity().runOnUiThread(() -> {
                userAdapter.setUserList(userList);
                userAdapter.notifyDataSetChanged();
            });
        });
    }
}
