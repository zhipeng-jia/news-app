package com.ihandy.a2013011373.newsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Utils {
    public static void showErrorDialog(Context context, int messageStringId,
                                       final Runnable callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.dialog_error));
        builder.setMessage(context.getResources().getString(messageStringId));
        builder.setPositiveButton(context.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.run();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
