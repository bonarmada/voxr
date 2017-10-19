package com.bombon.voxr.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Created by bonbonme on 10/20/2017.
 */

public class Util {

    public static void displayAlert(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        if (listener == null) {
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        } else {
            alertDialog.setPositiveButton("Ok", listener);
        }
        alertDialog.show();
    }

    public static void displayAlert(Context context, String title,
                                    String message, String pMessage, String nMessage, DialogInterface.OnClickListener pAction) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if (title != null)
            alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if (pAction != null)
            alertDialog.setPositiveButton(pMessage, pAction);
        else
            alertDialog.setPositiveButton(pMessage, null);

        alertDialog.setNegativeButton(nMessage, null);
        AlertDialog alert = alertDialog.create();
        alert.show();

        // Set Negative button to gray
        Button yButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        yButton.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
    }

}
