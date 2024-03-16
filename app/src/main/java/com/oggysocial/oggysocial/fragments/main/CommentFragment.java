package com.oggysocial.oggysocial.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.adapters.CommentAdapter;
import com.oggysocial.oggysocial.models.Comment;
import com.oggysocial.oggysocial.models.Post;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentFragment extends Fragment {

    RecyclerView recyclerView;

    Post post;


    public CommentFragment() {
    }

    public CommentFragment(Post post) {
        this.post = post;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        loadData();
        initListener();
    }

    private void initView() {
        recyclerView = requireView().findViewById(R.id.rvComments);
    }

    private void initListener() {
        
    }

    private void loadData() {

        List<Comment> comments = post.getComments();
        CommentAdapter adapter = new CommentAdapter(comments);
        recyclerView.setAdapter(adapter);
    }

}