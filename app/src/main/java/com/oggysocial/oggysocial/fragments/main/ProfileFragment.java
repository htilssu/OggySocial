package com.oggysocial.oggysocial.fragments.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ProfileFragment extends Fragment {

    PostAdapter postAdapter;
    View v;

    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postAdapter = new PostAdapter((ArrayList<Post>) generateDummyPosts());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        RecyclerView postRecyclerView = v.findViewById(R.id.rvPosts);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setAdapter(postAdapter);

        return v;
    }

    private List<Post> generateDummyPosts() {
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> {
                    Post post = new Post();
                    post.setAuthor("Author " + i);
                    post.setContent("Content " + i);
                    return post;
                }).collect(Collectors.toList());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postAdapter = null;
        v = null;
    }
}