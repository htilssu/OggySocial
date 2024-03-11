package com.oggysocial.oggysocial.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.EditProfile;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ProfileFragment extends Fragment {

    WeakReference<ProfileFragment> instance;
    PostAdapter postAdapter;
    List<Post> postList;
    MaterialButton btnEditProfile;
    RecyclerView postRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvUsername;
    View v;

    public ProfileFragment() {
    }

    public ProfileFragment getInstance() {
        return instance.get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = new WeakReference<>(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();

        return v;
    }

    private void initViews() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (postList == null) {
            PostService.getUserPosts(posts -> {
                postAdapter = new PostAdapter(posts);
                postRecyclerView.setAdapter(postAdapter);
                postList = posts;
            });
        } else {
            postAdapter = new PostAdapter(postList);
            postRecyclerView.setAdapter(postAdapter);
        }

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        tvUsername = v.findViewById(R.id.tvUsername);
        btnEditProfile = v.findViewById(R.id.btnEditProfile);

        initData();
        initListeners();

    }

    private void initData() {
        UserService.getUser(user -> {
            tvUsername.setText(user.getFullName());
        });
    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                PostService.getUserPosts(posts -> {
                    postAdapter = new PostAdapter(posts);
                    postRecyclerView.setAdapter(postAdapter);
                    postList = posts;
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
        });
        btnEditProfile.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        v = null;
    }

    public void addPost(Post post) {
        if (postList == null) {
            postList = new ArrayList<>();
        }
        postList.add(0, post);
    }


}
