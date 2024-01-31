package com.oggysocial.oggysocial.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.oggysocial.oggysocial.models.Status;
import com.oggysocial.oggysocial.models.User;
import com.oggysocial.oggysocial.services.FirebaseDB;

import java.util.Objects;

public class UserController {
    static FirebaseDatabase database = FirebaseDB.getDatabase();

    public static void addUser(User user) {
        database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);
    }
}
