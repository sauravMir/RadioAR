package com.educareapps.mylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ibrar on 4/25/2017.
 */

public class CommonDialog {

    public static void generalDialog(final Activity activity, String text) {
        final Dialog dialog = new Dialog(activity, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.general_dialog);
        dialog.setCancelable(false);


        final TextView tvBackPermission = (TextView) dialog.findViewById(R.id.tvBackPermission);
        tvBackPermission.setText(text);
        Button btnCancelPermission = (Button) dialog.findViewById(R.id.btnCancelPermission);
        Button btnOkPermission = (Button) dialog.findViewById(R.id.btnOkPermission);


        btnOkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        btnCancelPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DialogNavBarHide.navBarHide(activity, dialog);


    }
}
