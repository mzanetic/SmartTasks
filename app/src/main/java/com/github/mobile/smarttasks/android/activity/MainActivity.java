package com.github.mobile.smarttasks.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.github.mobile.smarttasks.R;
import com.github.mobile.smarttasks.android.fragment.DayFragment;
import com.github.mobile.smarttasks.android.ui.adapter.PagerAdapter;
import com.github.mobile.smarttasks.core.content.ContentController;
import com.github.mobile.smarttasks.core.database.DBManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.sql.SQLException;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @ViewById(R.id.view_pager)
    ViewPager viewPager;

    @Bean
    ContentController contentController;

    @Bean
    DBManager dbManager;

    PagerAdapter pagerAdapter;


    @AfterViews
    void init() {
        try {
            if (dbManager.getTaskCount() == 0) {
                getData();
            } else contentController.loadExistingData();
            setupAdapter();
        } catch (SQLException e) {
            showAlertDialog("Error occurred:\nCannot display tasks.");
            Log.e(LOG_TAG, "Database error: " + e.getLocalizedMessage());
        }
    }

    @UiThread
    void setupAdapter() {
        pagerAdapter = new PagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.getCurrentDatePosition());
    }

    @Background
    void getData() {
        try {
            contentController.getAndStoreTasks();
            setupAdapter();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot fetch tasks: " + e.getLocalizedMessage());
            showAlertDialog("Error occurred:\nCannot display tasks.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @OnActivityResult(DetailsActivity.REQUEST_CODE)
    void onResult(Intent data) {
        if (data == null || data.getStringExtra(DetailsActivity.ACTIVITY_RESULT) == null) return;
        if (data.getStringExtra(DetailsActivity.ACTIVITY_RESULT).equals(DetailsActivity.NO_CHANGE))
            return;
        if (data.getStringExtra(DetailsActivity.ACTIVITY_RESULT).equals(DetailsActivity.CHANGED)) {
            // refresh the controller and adapters
            String key = data.getStringExtra(DetailsActivity.CHANGE_KEY);
            contentController.refreshDay(key);
//
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.day_linear_root);
//            if (fragment != null && fragment instanceof DayFragment) {
//                Log.i(LOG_TAG, "Found fragment, refreshing...");
//                ((DayFragment) fragment).refreshData();
//            }

            sendBroadcast(new Intent(DayFragment.REFRESH_ACTION));
        }
    }
}
