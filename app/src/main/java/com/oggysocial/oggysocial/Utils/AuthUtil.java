package com.oggysocial.oggysocial.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtil {
    public static boolean isLoggedIn() {
        return false;
    }

    public static boolean isEmailValid(String email) {
        return false;
    }

    public static boolean isUserVerified() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return user.isEmailVerified();
        } else {
            return false;
        }
    }

    public static void sendVerificationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification();
        }
    }
}
