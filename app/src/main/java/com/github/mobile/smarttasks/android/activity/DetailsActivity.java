package com.github.mobile.smarttasks.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mobile.smarttasks.R;
import com.github.mobile.smarttasks.android.ui.TaskItemView;
import com.github.mobile.smarttasks.android.ui.TaskItemView_;
import com.github.mobile.smarttasks.core.content.ContentController;
import com.github.mobile.smarttasks.core.models.Status;
import com.github.mobile.smarttasks.core.models.TaskItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_details)
public class DetailsActivity extends Activity {

    public static final int    REQUEST_CODE    = 42;
    public static final String ACTIVITY_RESULT = "activityResult";
    public static final String CHANGED         = "changed";
    public static final String NO_CHANGE       = "noChange";
    public static final String CHANGE_KEY      = "change:key_value";

    @Extra
    TaskItem taskItem;

    @ViewById(R.id.placeholder)
    FrameLayout placeholder;

    @ViewById(R.id.buttons_linear)
    LinearLayout buttonsLinear;

    @ViewById(R.id.state_sign)
    ImageView sign;

    @ViewById(R.id.comment_linear)
    LinearLayout commentsLinear;

    @ViewById(R.id.comment_edit)
    EditText commentEdit;

    @Bean
    ContentController controller;


    boolean statusChange;

    @AfterViews
    void init() {
        addTaskView(false);
    }

    /**
     * Adds the task view to the placeholder
     * @param refresh False only after the initial loading, true otherwise
     */
    void addTaskView(boolean refresh) {
        if (refresh) placeholder.removeAllViews();

        TaskItemView taskItemView = TaskItemView_.build(this);
        taskItemView.bind(taskItem);
        taskItemView.showDetails(taskItem);
        placeholder.addView(taskItemView);

        if (taskItem.getStatus().equals(Status.UNRESOLVED) || refresh) {
            sign.setVisibility(View.GONE);
            commentsLinear.setVisibility(View.GONE);

            if (refresh)
                sign.setVisibility(View.VISIBLE);
            return;
        }

        sign.setVisibility(View.VISIBLE);
        changeDrawableState(taskItem.getStatus());
        buttonsLinear.setVisibility(View.GONE);
    }

    @Click(R.id.btn_resolve)
    void resolve() {
        showAlertDialog(Status.RESOLVED);
    }

    @Click(R.id.cant_resolve)
    void cantResolve() {
        showAlertDialog(Status.CANNOT_RESOLVE);
    }

    void saveStatus(Status status) {
        statusChange = true;
        buttonsLinear.setVisibility(View.GONE);
        taskItem.setStatus(status);
        controller.updateTask(taskItem);
    }

    void changeDrawableState(Status status) {
        Drawable drawable;
        if (status.equals(Status.RESOLVED))
            drawable = getDrawableVersionSafe(R.drawable.sign_resolved);
        else drawable = getDrawableVersionSafe(R.drawable.unresolved_sign);

        sign.setImageDrawable(drawable);
        sign.setVisibility(View.VISIBLE);

    }

    Drawable getDrawableVersionSafe(int drawableInt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawable(drawableInt);
        } else
            return getResources().getDrawable(drawableInt);
    }

    @Override
    public void onBackPressed() {
        backToCallingActivity();
    }

    void backToCallingActivity() {
        Intent intent = new Intent();
        if (statusChange) {
            intent.putExtra(ACTIVITY_RESULT, CHANGED);
            intent.putExtra(CHANGE_KEY, taskItem.getPageKey());
        } else intent.putExtra(ACTIVITY_RESULT, NO_CHANGE);
        setResult(REQUEST_CODE, intent);
        finish();
    }

    @UiThread
    void showAlertDialog(final Status status) {
        new AlertDialog.Builder(this)
                .setTitle("Comment")
                .setMessage("Would you like to leave a comment?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        enterEditMode(status);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveStatus(status);
                        addTaskView(true);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void enterEditMode(Status status) {
        buttonsLinear.setVisibility(View.INVISIBLE);
        commentsLinear.setVisibility(View.VISIBLE);
        taskItem.setStatus(status);
    }

    @Click(R.id.comment_save_btn)
    void saveComment() {
        taskItem.setComment(commentEdit.getText().toString());
        saveStatus(taskItem.getStatus());
        addTaskView(true);
    }

}
