package com.oggysocial.oggysocial.services;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    static FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobilesocialapp-1668a-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public static FirebaseDatabase getDatabase() {
        return database;
    }

}
