package com.example.onlineelectronicgadget.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlineelectronicgadget.R;

public class CustomAlertDialog {

    public interface OnDialogListener {
        void onClick(boolean flag);
    }

    public static void showDialog(Context context, String title, String msg, OnDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Yes", ((dialog, which) -> listener.onClick(true)))
                .setNegativeButton("No", ((dialog, which) -> listener.onClick(false)));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showCustomDialog(Context context, String title, String msg) {
        Dialog dialog =new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        TextView dialog_message = dialog.findViewById(R.id.dialog_message);
        Button dialog_button = dialog.findViewById(R.id.dialog_button);
        dialog.getWindow().setLayout(-1, -2);
        dialog_title.setText(title);
        dialog_message.setText(msg);
        dialog_button.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
