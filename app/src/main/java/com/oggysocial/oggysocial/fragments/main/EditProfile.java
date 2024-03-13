package com.oggysocial.oggysocial.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;

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
import com.oggysocial.oggysocial.services.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends Fragment {

    EditText edBio,edProfession;
    Button button;
    DocumentReference documentReference;
    User user1;
    View v;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String uid = user.getUid();

        documentReference = db.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user1 = documentSnapshot.toObject(User.class);
            }
        });
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_edit_profile, container, false);
        initViews();

        return v;
    }

    private void initViews() {
        edBio = v.findViewById(R.id.Edit_Education);
        edProfession = v.findViewById((R.id.Edit_bio));
        button = v.findViewById(R.id.btn_up);

        loadData();
        initListener();
    }

    private void initListener() {
        button.setOnClickListener(v -> {
            updateProfile();
        });
    }

    private void loadData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            documentReference = db.collection("user").document(uid);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                user1 = documentSnapshot.toObject(User.class);
            });
        }
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

