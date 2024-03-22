package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.NotifyAdapter;
import com.oggysocial.oggysocial.models.FriendRequest;
import com.oggysocial.oggysocial.models.Notify;
import com.oggysocial.oggysocial.services.FriendService;

import java.util.List;

public class NotifyFragment extends Fragment {

    RecyclerView rvNotify;

    List<? extends Notify> friendRequests;
    NotifyAdapter notifyAdapter;

    public NotifyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        loadRequests();
    }

    private void initViews() {
        rvNotify = requireView().findViewById(R.id.rvNotify);
        rvNotify.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadRequests() {
        FriendService.getRequest(requireContext(), friendRequests -> {
            this.friendRequests = friendRequests;
            setupAdapter();
        });
    }


    private void setupAdapter() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (notifyAdapter == null) {
                notifyAdapter = new NotifyAdapter(friendRequests);
                rvNotify.setAdapter(notifyAdapter);
            } else {
                notifyAdapter.setNotifyList(friendRequests);
                for (int i = 0; i < friendRequests.size(); i++) {
                    notifyAdapter.notifyItemChanged(i);
                }
            }
        });
    }

}