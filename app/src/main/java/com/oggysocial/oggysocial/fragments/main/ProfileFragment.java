package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.MainActivity;
import com.oggysocial.oggysocial.activities.PopupActivity;
import com.oggysocial.oggysocial.adapters.FriendAdapter;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Popup;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.FriendService;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    WeakReference<ProfileFragment> instance;
    PostAdapter postAdapter;
    FriendAdapter friendAdapter;
    CircleImageView civAvatar;
    ImageView ivBack;
    List<Post> postList;
    List<User> friendList;
    MaterialButton btnEditProfile, btnAddFriend, btnCreatePost;
    RecyclerView postRecyclerView, rvFriend;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout appBarLayout;
    TextView tvUsername, tvBirthDay, tvBio;
    ActivityResultLauncher<PickVisualMediaRequest> pickImage;
    View v;
    User user;
    boolean isMyProfile = false;
    boolean showAppBar = true;
    boolean isFriend = false;
    boolean isRequest = false;

    public ProfileFragment() {
        showAppBar = false;
    }

    public ProfileFragment(User user, boolean showAppBar) {
        this.user = user;
        isFriend = user.getFriends().contains(FirebaseAuth.getInstance().getUid());
        FriendService.checkRequestExists(FirebaseAuth.getInstance().getUid(), user.getId(), exists -> {
            isRequest = exists;
        });

        this.showAppBar = showAppBar;

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
        if (this.user == null) {
            UserService.getUser(user -> {
                this.user = user;
                isMyProfile = true;
            });
        }

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

        rvFriend = v.findViewById(R.id.rvFriends);
        rvFriend.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        friendAdapter = new FriendAdapter(friendList, FriendAdapter.FriendLayoutType.GRID);
        rvFriend.setAdapter(friendAdapter);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        tvBirthDay = v.findViewById(R.id.tvBirthday);
        tvUsername = v.findViewById(R.id.tvUsername);
        tvBio = v.findViewById(R.id.tvBio);

        civAvatar = v.findViewById(R.id.civAvatar);


        appBarLayout = v.findViewById(R.id.action_bar);
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
        if (user != null) {
            tvUsername.setText(user.getFullName());
            tvBirthDay.setText("Sinh ngày " + user.getBirthday());
            tvBio.setText(user.getBio() == null ? "" : user.getBio());
        }


        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(civAvatar);
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

        if (isFriend) {
            btnAddFriend.setText(R.string.remove_friend);
        } else {
            if (isRequest) {
                btnAddFriend.setText(R.string.cancel_request);
            } else {
                btnAddFriend.setText(R.string.add_friend);
            }
        }

        //Get friend list
        UserService.getFriends(user.getId(), friends -> {
            friendList = friends;
            friendAdapter.setFriends(friendList);
            if (friends == null) {
                rvFriend.setVisibility(View.VISIBLE);

            }
            ((TextView) requireView().findViewById(R.id.tvFriendCount)).setText(String.valueOf(friends == null ? 0 : friends.size()));

            new Handler(Looper.getMainLooper()).post(() -> friendAdapter.notifyDataSetChanged());
        });
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
            if (user.equals(UserService.user)) {
                main.showPickAvatarImage();
            }
        });

        btnCreatePost.setOnClickListener(v -> {
            showCreatePost();
        });

        btnAddFriend.setOnClickListener(v -> {
            if (isFriend) {
                FriendService.removeFriend(user.getId());
                isFriend = false;
            } else {
                if (isRequest) {
                    FriendService.rejectRequest(FirebaseAuth.getInstance().getUid(), user.getId());
                    isRequest = false;
                } else {
                    FriendService.sendRequest(FirebaseAuth.getInstance().getUid(), user.getId());
                    isRequest = true;
                }
            }

            updateAddFriendButton();
        });
    }

    private void updateAddFriendButton() {
        if (isFriend) {
            btnAddFriend.setText(R.string.remove_friend);
        } else {
            if (isRequest) {
                btnAddFriend.setText(R.string.cancel_request);
            } else {
                btnAddFriend.setText(R.string.add_friend);
            }
        }
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

    private void showCreatePost() {
        Intent intent = new Intent(getContext(), PopupActivity.class);
        intent.putExtra("popup", Popup.CREATE_POST);
        startActivity(intent);
    }

}
