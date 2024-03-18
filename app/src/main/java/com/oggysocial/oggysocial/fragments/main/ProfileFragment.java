package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.MainActivity;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.ImageService;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    WeakReference<ProfileFragment> instance;
    PostAdapter postAdapter;
    CircleImageView civAvatar;
    ImageView ivBack;
    List<Post> postList;
    MaterialButton btnEditProfile, btnAddFriend, btnCreatePost;
    RecyclerView postRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    AppBarLayout appBarLayout;
    TextView tvUsername;
    ActivityResultLauncher<PickVisualMediaRequest> pickImage;
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

    @SuppressLint("NotifyDataSetChanged")
    private void initViews() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        tvUsername = v.findViewById(R.id.tvUsername);
        civAvatar = v.findViewById(R.id.civAvatar);
        appBarLayout = v.findViewById(R.id.appBarLayout);
        if (!showAppBar) {
            appBarLayout.setVisibility(View.GONE);
        }
        ivBack = v.findViewById(R.id.ivBack);
        btnEditProfile = v.findViewById(R.id.btnEditProfile);
        btnCreatePost = v.findViewById(R.id.btnCreatePost);
        btnAddFriend = v.findViewById(R.id.btnAddFriend);


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

        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(civAvatar);
        } else {
            Glide.with(this).load(R.drawable.default_avatar).into(civAvatar);
        }


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

        civAvatar.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            assert main != null;
            main.showPickAvatarImage();
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
