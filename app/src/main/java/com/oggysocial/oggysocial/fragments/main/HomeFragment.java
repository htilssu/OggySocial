package com.oggysocial.oggysocial.fragments.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.activities.MainActivity;
import com.oggysocial.oggysocial.activities.PopupActivity;
import com.oggysocial.oggysocial.adapters.AddFriendAdapter;
import com.oggysocial.oggysocial.adapters.PostAdapter;
import com.oggysocial.oggysocial.models.Popup;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    TextView tvCreatePost;
    CircleImageView civAvatar;
    MaterialToolbar toolbar;
    List<Post> postList;
    PostAdapter postAdapter;
    View v;

    RecyclerView postRecyclerView;
    //goi ý kết bạn
    private RecyclerView rcViewAddFriend;
    //goi ý kết bạn
    private List<User> userList;
    private AddFriendAdapter addfrAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

//        postRecyclerView.setAdapter(postAdapter);
    }


    private void initView() {
        postRecyclerView = v.findViewById(R.id.rvPosts);
        postAdapter = new PostAdapter(postList);
        tvCreatePost = v.findViewById(R.id.tvCreatePost);
        toolbar = v.findViewById(R.id.adminoToolbar);
        civAvatar = v.findViewById(R.id.civAvatar);
        UserService.getUser(user -> {
            if (user.getAvatar() != null) {
                Glide.with(requireContext()).load(user.getAvatar()).into(civAvatar);
            }

        });
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setAdapter(postAdapter);
        //goi y ket ban
        rcViewAddFriend = v.findViewById(R.id.rcViewAddFriend);
        rcViewAddFriend.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)); // Đổi LinearLayoutManager thành chiều ngan

        if (userList == null) {
            userList = new ArrayList<>();
        }
        // Tạo một implementation của interface OnItemClickListener
        AddFriendAdapter.OnItemClickListener itemClickListener = position -> {
            // Xử lý sự kiện click user để xem profile
            User user = userList.get(position);
            ProfileFragment profileFragment = new ProfileFragment(user, true);
            getParentFragmentManager().beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainerView, profileFragment).commit();
        };
        // Khởi tạo adapter và thiết lập listener
        if (addfrAdapter == null) {
            addfrAdapter = new AddFriendAdapter(userList, itemClickListener);
        }
        rcViewAddFriend.setAdapter(addfrAdapter);
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
        PostService.getNewFeeds(posts -> {
            if (postAdapter != null) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    postAdapter.setPosts(posts);
                    postList = posts;
                    postAdapter.notifyDataSetChanged();
                });
            }
        });
        postAdapter.setPosts(postList);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tất cả các user từ Firestore
        db.collection("users").addSnapshotListener((value, error) -> {
            if (value != null) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    List<User> listUser = value.toObjects(User.class);

                    UserService.getUser(user -> {
                        List<String> friendList = new ArrayList<>(UserService.user.getFriends());
                        friendList.add(UserService.user.getId());

                        listUser.removeIf(user1 -> user.equals(user1) || friendList.contains(user1.getId()));
                        userList = listUser;
                        addfrAdapter.setUserList(listUser);
                        addfrAdapter.notifyDataSetChanged();
                    });

                });
            }
        });
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