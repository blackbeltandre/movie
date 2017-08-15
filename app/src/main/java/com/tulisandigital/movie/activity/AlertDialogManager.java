package com.tulisandigital.movie.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tulisandigital.movie.R;

/**
 * Developed with love for completing competence android developer by UDACITY
 * Project beasiswa android developer dari udacity by Andre Marbun @developerpdak
 * Website : http://tulisandigital.com
 */
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.mipmap.ic_action_bell : R.mipmap.ic_action_bell);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}