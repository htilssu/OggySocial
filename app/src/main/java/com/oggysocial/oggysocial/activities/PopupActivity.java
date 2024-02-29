package com.oggysocial.oggysocial.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.fragments.main.CreatePostFragment;
import com.oggysocial.oggysocial.models.Popup;

public class PopupActivity extends AppCompatActivity {

    Popup popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).addToBackStack(null).replace(R.id.fragmentContainerView, CreatePostFragment.class, null).commit();
                break;
        }
    }

    private void getData() {
        popup = getIntent().getSerializableExtra("popup", Popup.class);
    }


}