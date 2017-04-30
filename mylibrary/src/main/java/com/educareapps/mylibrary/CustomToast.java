package com.educareapps.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by ibrar on 8/11/2016.
 */
public class CustomToast {

    public static void logD(String message) {
        Log.d("test", "" + message);
    }

    public static void logE(String str, String message) {
        Log.e(str, message);
    }

    public static void shortMessage(Context ctx, String message) {

        Intent intent = new Intent(ctx, TransparentToastActivity.class);
        intent.putExtra("message", message);
        ctx.startActivity(intent);

    }

}
