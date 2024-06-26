package com.oggysocial.oggysocial.activities;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.CreatePostFragment;
import com.oggysocial.oggysocial.fragments.main.UpdatePostFragment;
import com.oggysocial.oggysocial.models.Popup;
import com.oggysocial.oggysocial.models.Post;

public class PopupActivity extends AppCompatActivity {

    Popup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainerView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getData();


        switch (popup) {
            case CREATE_POST:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, CreatePostFragment.class, null).commit();
                break;
            case UPDATE_POST:
                Post post = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    post = getIntent().getSerializableExtra("post", Post.class);
                } else {
                    post = (Post) getIntent().getSerializableExtra("post");
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, UpdatePostFragment.class, bundle).commit();
                break;
        }
    }

    private void getData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            popup = getIntent().getSerializableExtra("popup", Popup.class);
        } else {
            popup = (Popup) getIntent().getSerializableExtra("popup");
        }
    }


}