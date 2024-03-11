package com.oggysocial.oggysocial.services;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.oggysocial.oggysocial.R;
import com.oggysocial.oggysocial.models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText edBio,edProfession;
    Button button;
    DocumentReference documentReference;
    User user1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String uid = user.getUid();

        documentReference = db.collection("user").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user1 = documentSnapshot.toObject(User.class);
                updateProfile();
            }
        });
        }

        edBio = findViewById(R.id.Edit_Education);
        edProfession = findViewById((R.id.Edit_Education));
        button = findViewById(R.id.btn_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    private void updateProfile() {


       String bio = edBio.getText().toString();
//        String prof = edProfession.getText().toString();
//
//        //CollectionReference cities = db.collection("cities");
//
////        Map<String, Object> data1 = new HashMap<>();
////        data1.put("bio", "San Francisco");
////        data1.put("profession", "Đã học tại THCS Tân Kiên");
////        data1.put("sexual", Arrays.asList("Nam","Nu","khác"));
//        //cities.document("SF").set(data1);
      user1.setBio(bio);
//        user1.setBio(prof);
        UserService.saveUser(user1);
    }

}

