package com.educareapps.mylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rarepebble.colorpicker.ColorPickerView;

import java.util.ArrayList;

/**
 * Created by ibrar on 4/26/2017.
 */

public class CustomColorSet {

    public static void showColorPicker(final Activity activity, final int which, final CircularTextView cTxt, final TextView txt) {

        final Dialog dialog = new Dialog(activity, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.color_picker_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ArrayList<CustomColorView> customColorViewArrayList = new ArrayList<>();

        final ColorPickerView colorPickerView = new ColorPickerView(activity);
        final RelativeLayout rlColorPicker = (RelativeLayout) dialog.findViewById(R.id.rlColorPicker);
        rlColorPicker.addView(colorPickerView);

        LinearLayout llItem = (LinearLayout) dialog.findViewById(R.id.llItem);
        final int[] colorPicker = activity.getResources().getIntArray(R.array.colorPicker);
        for (int i = 0; i < colorPicker.length; i++) {
            CustomColorView customColorView = new CustomColorView(activity);
            customColorView.setId(i);
            customColorView.setTxtId(i);
            customColorView.setTag("Top");
            customColorView.setPicColor(i);
            customColorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomColorView colorView = (CustomColorView) v;
                    final int color = colorView.getBackgroundColor();
                    colorPickerView.setColor(colorPicker[colorView.getId()]);
                }
            });
            customColorViewArrayList.add(customColorView);
            llItem.addView(customColorView);
        }

       /* if (circularTextView != null)
            colorPickerView.setColor(circularTextView.getSolidColor());*/

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (which == 0) {
                    txt.setBackgroundColor(colorPickerView.getColor());
                } else if (which == 1) {
                    cTxt.setSolidColor(colorPickerView.getColor());
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        DialogNavBarHide.navBarHide(activity, dialog);

    }

}
