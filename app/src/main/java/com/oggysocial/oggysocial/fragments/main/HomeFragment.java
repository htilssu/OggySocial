package com.oggysocial.oggysocial.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.PopupActivity;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Popup;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.PostService;

import java.util.List;

public class HomeFragment extends Fragment {
    PostAdapter postAdapter;
    View v;
    TextView tvCreatePost;
    List<Post> postList;
    private RecyclerView postRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        postAdapter = new PostAdapter((ArrayList<Post>) generateDummyPosts());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        postRecyclerView = view.findViewById(R.id.rvPosts);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        postRecyclerView.setAdapter(postAdapter);
    }


    private void initView() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        tvCreatePost = v.findViewById(R.id.tvCreatePost);

        loadData();
        initListener();
    }

    private void initListener() {
        //show create post fragment
        tvCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PopupActivity.class);
            intent.putExtra("popup", Popup.CREATE_POST);
            startActivity(intent);
        });
    }

    private void loadData() {
        PostService.getNewFeeds(posts -> {
            postAdapter = new PostAdapter(posts);
            postRecyclerView.setAdapter(postAdapter);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postAdapter = null;
        postRecyclerView = null;
    }
}