package com.oggysocial.oggysocial.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oggysocial.oggysocial.models.Post;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Executable;
import java.util.UUID;
import java.util.function.Function;

public class ImageService {

    static String imageRefPath = "/users/%userId%/images/";
    static FirebaseStorage storage = FirebaseDB.getStorage();

    public static void uploadImage(Uri imageUri, Function<Uri, Void> callback) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            StorageReference imageRef = storage
                    .getReference()
                    .child(imageRefPath.replace("%userId%", userId))
                    .child(UUID.randomUUID().toString());

            callback.apply(imageRef.getDownloadUrl().getResult());
        }


    }

    public static ActivityResultLauncher<PickVisualMediaRequest> getPickMedia(Context context, Function<Uri, Void> callback) {
        AppCompatActivity activity = (AppCompatActivity) context;

        return activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), callback::apply);
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException ignored) {
        }
        return bitmap;
    }
}
