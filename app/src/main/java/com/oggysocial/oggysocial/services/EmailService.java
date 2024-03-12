package com.oggysocial.oggysocial.services;

import com.google.firebase.firestore.FirebaseFirestore;

public class EmailService {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.]+[+]?[a-zA-Z0-9]+@[a-z0-9]+[.][a-z]+$";
        return email.matches(emailRegex);
    }

    /**
     * Kiểm tra email đã tồn tại trong hệ thống chưa
     *
     * @param mail     email cần kiểm tra
     * @param listener callback trả về kết quả kiểm tra
     * @callback-param true nếu email chưa tồn tại
     * false nếu email đã tồn tại
     */
    public static void checkEmailExist(String mail, OnEmailCheckedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("email", mail).get().addOnSuccessListener(queryDocumentSnapshots -> {
            listener.onEmailChecked(!queryDocumentSnapshots.isEmpty());
        });
    }

    public interface OnEmailCheckedListener {
        void onEmailChecked(boolean isExist);
    }
}

