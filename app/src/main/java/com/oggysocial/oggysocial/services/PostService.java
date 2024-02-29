package com.oggysocial.oggysocial.services;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostService {
    static FirebaseStorage storage = FirebaseDB.getStorage();

    public static void uploadImage(Uri imageUri) {
        StorageReference imageRef = storage.getReference().child("images");
        imageRef.putFile(imageUri);
    }
}
