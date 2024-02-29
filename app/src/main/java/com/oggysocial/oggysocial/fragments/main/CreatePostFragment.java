package com.oggysocial.oggysocial.fragments.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.Post;
import com.oggysocial.oggysocial.services.ImageService;

import java.util.ArrayList;


public class CreatePostFragment extends Fragment {

    View v;
    MaterialButton btnPostIt, btnClose, btnPickImage;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    ImageView ivPostImage;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    EditText etPostContent;

    String mime = "image/*";
    Uri imageUri;


    public CreatePostFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_post, container, false);
        initView();
        pickMedia = ImageService.getPickMedia(requireContext(), uri -> {
            try {
                imageUri = uri;
                ivPostImage.setImageURI(imageUri);
                return null;
            } catch (Exception e) {
                return null;
            }
        });
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
    }


    private void initListener() {
        btnClose.setOnClickListener(v -> requireActivity().finish());
        btnPickImage.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mime)).build()));
        btnPostIt.setOnClickListener(v -> {
            Post newPost = new Post();
            ImageService.uploadImage(imageUri, uri -> {
                newPost.setAuthor(FirebaseAuth.getInstance().getUid());
                newPost.setContent(etPostContent.getEditableText().toString());
                newPost.setImages(new ArrayList<>() {
                    {
                        add(uri.toString());
                    }
                });
                return null;
            });

            newPost.setAuthor(FirebaseAuth.getInstance().getUid());
            newPost.setContent(etPostContent.getEditableText().toString());
        });
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(80);
    }
}