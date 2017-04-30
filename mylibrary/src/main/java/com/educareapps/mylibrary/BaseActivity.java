package com.educareapps.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends Activity {

    BaseActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        fullScreencall();
        UiChangeListener();

        //Custom Error log handeled by Mail
     if (!ApplicationMode.devMode) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        }
    }

    // for centrally handled String type by Rokan
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // Full Screen display
    public void fullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    // Navigation bar control
    public void UiChangeListener() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    // Device back pressed
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreencall();
        UiChangeListener();
    }

}
