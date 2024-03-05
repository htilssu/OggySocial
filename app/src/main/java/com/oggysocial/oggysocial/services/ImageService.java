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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class ImageService {

    /**
     * Đường dẫn lưu trữ ảnh trên storage
     */
    static String imageRefPath = "/users/%userId%/images/";
    static FirebaseStorage storage = FirebaseDB.getStorage();

    /**
     * @param imageUri Uri của file cần upload lên storage
     * @param callback Hàm callback sẽ được gọi khi upload thành công, trả về StorageReference của file đã upload
     * @throws RuntimeException Nếu upload thất bại
     */
    public static void uploadImage(Uri imageUri, Function<StorageReference, Void> callback) throws RuntimeException {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            StorageReference imageRef = storage.getReference().child(imageRefPath.replace("%userId%", userId)).child(UUID.randomUUID().toString());

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> callback.apply(imageRef)).addOnFailureListener(l -> {
                try {
                    throw l;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Lấy StorageReference của file ảnh từ id của file với người dùng có id là userId
     *
     * @param userId  Id của người dùng
     * @param imageId Id của file cần lấy
     * @return StorageReference của file cần lấy
     */
    public static StorageReference getImageRef(String userId, String imageId) {
        return storage.getReference().child(imageRefPath.replace("%userId%", userId)).child(imageId);
    }

    /**
     * Lấy StorageReference của file ảnh từ id của file với người đăng nhập hiện tại
     *
     * @param imageId Id của file cần lấy
     * @return StorageReference của file cần lấy
     */
    public static StorageReference getImageRef(String imageId) {
        String userId = FirebaseAuth.getInstance().getUid();
        return getImageRef(userId, imageId);
    }

    public static void deleteImage(String imageRefPath, OnDeletedImageListener listener) {
        storage.getReference().child(imageRefPath.replace("%userId%", Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))).delete().addOnSuccessListener(unused -> listener.onDeletedImage());
    }

    /**
     * @param context  Context của activity hoặc fragment
     * @param callback Hàm callback sẽ được gọi khi người dùng chọn file, trả về Uri của file đã chọn
     * @return ActivityResultLauncher để sử dụng trong hàm onActivityResult
     * @throws ClassCastException Nếu context không phải là AppCompatActivity
     * @apiNote Phải gọi trước khi state của activity chuyển sang resumed
     */
    public static ActivityResultLauncher<PickVisualMediaRequest> getPickMedia(Context context, OnImageSelectedListener callback) throws ClassCastException {
        AppCompatActivity activity = (AppCompatActivity) context;

        return activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), callback::onImageSelected);
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

    public interface OnDeletedImageListener {
        void onDeletedImage();
    }

    public interface OnImageSelectedListener {
        void onImageSelected(Uri uri);
    }
}
