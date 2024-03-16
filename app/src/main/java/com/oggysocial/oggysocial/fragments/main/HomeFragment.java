package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.MainActivity;
import com.oggysocial.oggysocial.activities.PopupActivity;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Popup;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.PostService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    PostAdapter postAdapter;
    View v;
    TextView tvCreatePost;

    CircleImageView civAvatar;
    MaterialToolbar toolbar;
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

//        postRecyclerView.setAdapter(postAdapter);
    }


    private void initView() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        postAdapter = new PostAdapter(null);
        tvCreatePost = v.findViewById(R.id.tvCreatePost);
        toolbar = v.findViewById(R.id.toolbar);
        civAvatar = v.findViewById(R.id.civAvatar);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setAdapter(postAdapter);

        loadData();
        initListener();
    }

    private void initListener() {
        //show create post fragment
        tvCreatePost.setOnClickListener(v -> {
            showCreatePost();
        });
        //Onclick on avatar
        civAvatar.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            assert main != null;
            main.showProfile();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search_item) {
                showSearch();
            }
            return true;
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        if (postList == null) {
            PostService.getNewFeeds(posts -> {
                if (postAdapter != null) {
                    postAdapter.setPosts(posts);
                    postList = posts;
                    postAdapter.notifyDataSetChanged();
                }
            });
        } else {
            postAdapter.setPosts(postList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showSearch() {
        getParentFragmentManager().beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainerView, new SearchFragment()).commit();
    }

    private void showCreatePost() {
        Intent intent = new Intent(getContext(), PopupActivity.class);
        intent.putExtra("popup", Popup.CREATE_POST);
        startActivity(intent);
    }
}