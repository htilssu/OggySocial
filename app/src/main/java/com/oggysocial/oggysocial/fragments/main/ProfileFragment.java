package com.oggysocial.oggysocial.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.PostService;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ProfileFragment extends Fragment {

    PostAdapter postAdapter;
    List<Post> listPosts;

    RecyclerView postRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View v;

    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();

        return v;
    }

    private void initViews() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (listPosts == null) {
            PostService.getUserPosts(posts -> {
                postAdapter = new PostAdapter(posts);
                postRecyclerView.setAdapter(postAdapter);
                listPosts = posts;
            });
        } else {
            postAdapter = new PostAdapter(listPosts);
            postRecyclerView.setAdapter(postAdapter);
        }

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        initListeners();
    }

    private void initData() {

    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                PostService.getUserPosts(posts -> {
                    postAdapter = new PostAdapter(posts);
                    postRecyclerView.setAdapter(postAdapter);
                    listPosts = posts;
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        v = null;
    }


}