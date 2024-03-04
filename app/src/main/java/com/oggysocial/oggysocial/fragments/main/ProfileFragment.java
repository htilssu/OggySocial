package com.oggysocial.oggysocial.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ProfileFragment extends Fragment {

    PostAdapter postAdapter;
    List<Post> listPosts;

    RecyclerView postRecyclerView;
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

        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        v = null;
    }


}