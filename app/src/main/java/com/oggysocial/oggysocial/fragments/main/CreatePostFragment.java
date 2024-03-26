package com.oggysocial.oggysocial.fragments.main;

import android.animation.Animator;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
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
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.ImageService;
import com.oggysocial.oggysocial.services.PostService;
import com.oggysocial.oggysocial.services.UserService;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreatePostFragment extends Fragment {

    View v;
    MaterialButton btnPostIt, btnClose, btnPickImage;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    ImageView ivPostImage;
    CircleImageView ivAuthorImage;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    TextView tvAuthorName;
    EditText etPostContent;
    LottieAnimationView lottieAnimationView;

    String mime = "image/*";
    Uri imageUri;


    public CreatePostFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slide slide = new Slide();
        slide.setDuration(200);
        slide.setSlideEdge(Gravity.END);
        setEnterTransition(slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_post, container, false);
        initView();
        pickMedia = ImageService.getPickMedia(requireContext(), uri -> {
            try {
                imageUri = uri;
                Glide.with(requireContext()).load(imageUri).into(ivPostImage);
            } catch (Exception ignored) {
            }
        });
        loadData();
        initListener();
        return v;
    }


    private void initView() {
        btnClose = v.findViewById(R.id.btnClose);
        btnPostIt = v.findViewById(R.id.btnPostIt);
        bottomSheetBehavior = BottomSheetBehavior.from(v.findViewById(R.id.llCreatePostOptions));
        setupBottomSheet();
        btnPickImage = v.findViewById(R.id.btnPickImage);
        ivPostImage = v.findViewById(R.id.ivPostImage);
        etPostContent = v.findViewById(R.id.etPostContent);
        lottieAnimationView = v.findViewById(R.id.animation_view);
        tvAuthorName = v.findViewById(R.id.tvAuthorName);
        ivAuthorImage = v.findViewById(R.id.ivAuthorImage);
    }


    private void initListener() {
        btnClose.setOnClickListener(v -> requireActivity().finish());
        btnPickImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mime)).build()));
        btnPostIt.setOnClickListener(v -> {
            showLoading();
            Post newPost = new Post();
            newPost.setContent(etPostContent.getEditableText().toString());
            if (imageUri != null) {
                try {
                    ImageService.uploadImage(requireContext(), imageUri, ref -> {
                        String name = ref.getName();
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            newPost.getImages().put(name, uri.toString());
                            PostService.savePost(newPost, p -> showSuccess());
                            imageUri = null;

                        });
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                PostService.savePost(newPost, post -> showSuccess());
            }
        });
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(80);
    }

    private void loadAnimation() {
        lottieAnimationView.setAnimation("loading.json");

    }

    private void showLoading() {
        lottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setMinAndMaxFrame(0, 320);
    }

    private void showSuccess() {
        lottieAnimationView.setRepeatCount(0);
        lottieAnimationView.setMinAndMaxFrame(320, 416);
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                requireActivity().finish();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    private void showFailure() {
        lottieAnimationView.setRepeatCount(1);
        lottieAnimationView.setMinAndMaxFrame(736, 841);
        lottieAnimationView.playAnimation();
    }

    private void loadData() {
        UserService.getUser(user -> {
            tvAuthorName.setText(user.getFullName());
            if (user.getAvatar() != null) {
                Glide.with(requireContext()).load(user.getAvatar()).into(ivAuthorImage);
            }
        });

    }
}