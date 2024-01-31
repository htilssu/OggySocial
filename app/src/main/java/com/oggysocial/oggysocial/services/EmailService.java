package com.oggysocial.oggysocial.services;

public class EmailService {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.]+[+]?[a-zA-Z0-9]+@[a-z0-9]+[.][a-z]+$";
        return email.matches(emailRegex);
    }
}

