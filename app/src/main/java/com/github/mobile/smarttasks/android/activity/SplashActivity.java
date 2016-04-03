package com.github.mobile.smarttasks.android.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.github.mobile.smarttasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    private static final int INTRO_DURATION = (int) TimeUnit.SECONDS.toMillis(3);

    @AfterViews
    void init() {
        Handler handler = new Handler();
        final Context context = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity_.intent(context).start();
                finish();
            }
        }, INTRO_DURATION);
    }
}
