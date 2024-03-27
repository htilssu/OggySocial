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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class ImageService {

    /**
     * Đường dẫn lưu trữ ảnh trên storage
     */
    static String imageRefPath = "/users/%userId%/images/";
    static FirebaseStorage storage = FirebaseDB.getStorage();

    /**
     * @param imageUri Uri của file cần upload lên storage
     * @param listener Hàm callback sẽ được gọi khi upload thành công, trả về StorageReference của file đã upload
     * @throws RuntimeException Nếu upload thất bại
     */
    public static void uploadImageSquare(Context context, Uri imageUri, OnUploadImageListener listener) throws RuntimeException, IOException {
        Bitmap bitmap = getBitmapFromUri(context, imageUri);
        bitmap = resizeToSquare(bitmap, 500, 500);
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            StorageReference imageRef = storage.getReference().child(imageRefPath.replace("%userId%", userId)).child(UUID.randomUUID().toString());

            imageRef.putStream(bitmapToInputStream(bitmap)).addOnSuccessListener(taskSnapshot -> listener.onUploadImage(imageRef));
        }
    }

    /**
     * Upload ảnh gốc lên storage
     *
     * @param imageUri Uri của file cần upload lên storage
     * @param listener Hàm callback sẽ được gọi khi upload thành công, trả về StorageReference của file đã upload
     */
    public static void uploadOriginImage(Uri imageUri, OnUploadImageListener listener) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            StorageReference imageRef = storage.getReference().child(imageRefPath.replace("%userId%", userId)).child(UUID.randomUUID().toString());
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> listener.onUploadImage(imageRef));
        }
    }

    /**
     * @param bitmap Ảnh cần chuyển thành InputStream
     * @return InputStream của ảnh
     */
    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        byte[] byteArray = bitmapToByteArray(bitmap);
        return new ByteArrayInputStream(byteArray);
    }

    /**
     * @param context Context của activity hoặc fragment
     * @param uri     Uri của file ảnh
     * @return Bitmap của file ảnh
     * @throws IOException Nếu không thể mở file ảnh
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * @param source Ảnh cần resize
     * @param width  Chiều rộng mới
     * @param height Chiều cao mới
     * @return Ảnh đã resize
     */
    public static Bitmap resizeToSquare(Bitmap source, int width, int height) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        return Bitmap.createBitmap(source, x, y, size, size);
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

    /**
     * Xóa ảnh từ storage
     *
     * @param imageRefPath Đường dẫn của ảnh cần xóa trên storage, nó phải là đường dẫn của ảnh trên storage
     * @param listener     Hàm callback sẽ được gọi khi ảnh đã xóa
     */
    public static void deleteImage(String imageRefPath, OnDeletedImageListener listener) {
        storage.getReference().child(imageRefPath.replace("%userId%", Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))).delete().addOnSuccessListener(unused -> listener.onDeletedImage());
    }

    /**
     * Xóa ảnh từ storage
     *
     * @param downloadUri Đường dẫn của ảnh cần xóa, nó là đường dẫn download ảnh
     */
    public static void deleteImage(String downloadUri) {
        storage.getReferenceFromUrl(downloadUri).delete();
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

    /**
     * Khởi tạo bitmap từ asset trong folder assets với đường dẫn được truyền vào
     *
     * @param context  Context của activity hoặc fragment
     * @param filePath Đường dẫn của file trong folder assets
     * @return Bitmap của file
     */
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

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public interface OnDeletedImageListener {
        void onDeletedImage();
    }

    public interface OnImageSelectedListener {
        void onImageSelected(Uri uri);
    }

    /**
     * Hàm callback sẽ được gọi khi upload ảnh lên storage thành công
     */
    public interface OnUploadImageListener {
        void onUploadImage(StorageReference ref);
    }

    public interface OnSetSelectedCallbackListener {
        void onSetSelectedCallback(OnImageSelectedListener callback);
    }
}
