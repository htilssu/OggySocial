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
    MaterialButton btnEditProfile, btnAddFriend, btnCreatePost;
    RecyclerView postRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvUsername;
    View v;
    User user;
    boolean isMyProfile = false;
    boolean showAppBar = true;

    public ProfileFragment() {
        UserService.getUser(user -> {
            this.user = user;
            isMyProfile = true;
        });
    }

    public ProfileFragment(User user) {
        this.user = user;
    }

    public void setShowAppBar(boolean showAppBar) {
        this.showAppBar = showAppBar;
    }

    public void setUser(User user) {
        this.user = user;
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
        civAvatar = v.findViewById(R.id.civAvatar);
        appBarLayout = v.findViewById(R.id.appBarLayout);
        if (!showAppBar) {
            appBarLayout.setVisibility(View.GONE);
        }
        ivBack = v.findViewById(R.id.ivBack);
        btnEditProfile = v.findViewById(R.id.btnEditProfile);

        //Hide add friend
        if (isMyProfile) {
            btnEditProfile.setVisibility(View.VISIBLE);
            btnCreatePost.setVisibility(View.VISIBLE);
            btnAddFriend.setVisibility(View.GONE);
        } else {
            btnEditProfile.setVisibility(View.GONE);
            btnCreatePost.setVisibility(View.GONE);
            btnAddFriend.setVisibility(View.VISIBLE);
        }

        initData();
        initListeners();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        tvUsername.setText(user.getFullName());
//        Glide.with(this).load(user.getAvatar()).into(civAvatar);
        //TODO: load avatar


        if (postList == null) {
            PostService.getUserPosts(user.getId(), posts -> {
                if (postAdapter == null) {
                    postAdapter = new PostAdapter(posts);
                    postRecyclerView.setAdapter(postAdapter);
                } else {
                    postAdapter.setPosts(posts);
                    postAdapter.notifyDataSetChanged();
                }
                postList = posts;
            });
        } else {
            postAdapter = new PostAdapter(postList);
            postRecyclerView.setAdapter(postAdapter);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            postAdapter.notifyDataSetChanged();
        });

        ivBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        btnEditProfile.setOnClickListener(v -> {
            EditProfile editProfileFragment = new EditProfile();

            Bundle bundle = new Bundle();
            bundle.putString("userId", user.getId());

            editProfileFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, editProfileFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
