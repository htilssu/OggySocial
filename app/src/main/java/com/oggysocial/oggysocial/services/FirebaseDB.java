package com.oggysocial.oggysocial.services;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseDB {
    static FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobilesocialapp-1668a-default-rtdb.asia-southeast1.firebasedatabase.app/");
    static FirebaseStorage storage = FirebaseStorage.getInstance("gs://mobilesocialapp-1668a.appspot.com");

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static FirebaseStorage getStorage() {
        return storage;
    }
}
