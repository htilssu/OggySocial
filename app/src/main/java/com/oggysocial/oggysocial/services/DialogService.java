package com.oggysocial.oggysocial.services;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogService {

    public static void showDialog(Context context, String title, String msg, String closeMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setNegativeButton(closeMsg, listener);
        builder.setOnDismissListener(dialog -> {
            listener.onClick(dialog, 0);
        });

        builder.show();
    }
}
