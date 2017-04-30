package com.educareapps.mylibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by RAFI on 12/21/2016.
 */

public class InternetAvailabilityCheck {

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == cm.TYPE_WIFI)
                return 1;

            if (activeNetwork.getType() == cm.TYPE_MOBILE)
                return 2;
        }
        return 0;
    }


}
