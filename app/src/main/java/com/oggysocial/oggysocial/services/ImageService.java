package com.oggysocial.oggysocial.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class ImageService {

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
