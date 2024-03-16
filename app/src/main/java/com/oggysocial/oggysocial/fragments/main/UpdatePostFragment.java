package com.oggysocial.oggysocial.fragments.main;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.ImageService;
import com.oggysocial.oggysocial.services.PostService;

import java.util.Map;

public class UpdatePostFragment extends Fragment {
    static OnUpdatedPost onUpdatedPost;
    MaterialButton btnPostIt, btnClose, btnPickImage;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    ImageView ivPostImage;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    EditText etPostContent;
    TextView tvAuthorName;
    String mime = "image/*";
    Uri imageUri;
    Post post;

    public UpdatePostFragment() {
    }

    public static OnUpdatedPost getOnUpdatedPost() {
        return onUpdatedPost;
    }

    public static void setOnUpdatedPost(OnUpdatedPost onUpdatedPost) {
        UpdatePostFragment.onUpdatedPost = onUpdatedPost;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            post = getArguments().getSerializable("post", Post.class);
        } else {
            post = (Post) getArguments().getSerializable("post");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        pickMedia = ImageService.getPickMedia(requireContext(), uri -> {
            try {
                imageUri = uri;
                ivPostImage.setImageURI(imageUri);
            } catch (Exception ignored) {
            }
        });
        initListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_update_post, container, false);
    }

    private void initView() {
        btnClose = requireView().findViewById(R.id.btnClose);
        btnPostIt = requireView().findViewById(R.id.btnPostIt);
        bottomSheetBehavior = BottomSheetBehavior.from(requireView().findViewById(R.id.llCreatePostOptions));
        tvAuthorName = requireView().findViewById(R.id.tvAuthorName);
        setupBottomSheet();
        btnPickImage = requireView().findViewById(R.id.btnPickImage);
        ivPostImage = requireView().findViewById(R.id.ivPostImage);
        etPostContent = requireView().findViewById(R.id.etPostContent);

        bidingData();
    }


    private void initListener() {
        btnClose.setOnClickListener(v -> requireActivity().finish());
        btnPickImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mime)).build()));
        btnPostIt.setOnClickListener(v -> {
            post.setContent(etPostContent.getText().toString());
            if (imageUri != null) {
                ImageService.uploadImage(imageUri, storageReference -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, String> imageList = post.getImages();
                        imageList.forEach((s, s2) -> {
                            ImageService.deleteImage(s2);
                        });
                        post.getImages().put(storageReference.getName(), String.valueOf(uri));
                        PostService.updatePost(post);
//                        onUpdatedPost.onUpdated();
                    });
                });
            } else {
                PostService.updatePost(post);
//                onUpdatedPost.onUpdated();
            }
            requireActivity().finish();
        });
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(80);
    }

    private void bidingData() {
        etPostContent.setText(post.getContent());
        tvAuthorName.setText(post.getUser().getFullName());
        if (!post.getImages().isEmpty()) {
            post.getImages().forEach((s, uri) -> Glide.with(requireView()).load(uri).into(ivPostImage));
        }
    }

    public interface OnUpdatedPost {
        void onUpdated();
    }
}
