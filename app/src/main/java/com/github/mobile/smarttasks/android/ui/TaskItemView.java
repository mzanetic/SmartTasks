package com.github.mobile.smarttasks.android.ui;


import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mobile.smarttasks.R;
import com.github.mobile.smarttasks.core.content.ContentController;
import com.github.mobile.smarttasks.core.models.Status;
import com.github.mobile.smarttasks.core.models.TaskItem;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@EViewGroup(R.layout.list_item)
public class TaskItemView extends RelativeLayout {

    @ViewById(R.id.root_layout)
    RelativeLayout rootLayout;

    @ViewById(R.id.title_text)
    TextView titleText;

    @ViewById(R.id.due_date_text)
    TextView dueDate;

    @ViewById(R.id.days_left_text)
    TextView daysLeft;

    @ViewById(R.id.priority)
    TextView priority;

    @ViewById(R.id.additional_linear)
    LinearLayout additional;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.status)
    TextView status;

    public TaskItemView(Context context) {
        super(context);
    }

    public void bind(TaskItem item) {
        titleText.setText(item.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat(ContentController.DATE_FORMAT, Locale.ENGLISH);
        dueDate.setText(dateFormat.format(item.getDueDate()));

        Calendar calendar = Calendar.getInstance();

        long dateDifference = item.getDueDate().getTime() - calendar.getTime().getTime();
        if (dateDifference < 0) {
            dateDifference = 0;
        }

        priority.setText(String.valueOf(item.getPriority()));

        daysLeft.setText(String.valueOf(TimeUnit.DAYS.convert(dateDifference, TimeUnit.MILLISECONDS)));
    }

    public void applyTheme(Status status) {
        int color;
        if (status.equals(Status.RESOLVED)) {
            color = R.color.app_green;
            setDrawableBackground(R.drawable.item_background_green);
        } else if (status.equals(Status.CANNOT_RESOLVE)) {
            color = R.color.app_red;
            setDrawableBackground(R.drawable.item_background_red);
        } else {
            color = R.color.app_red;
            setDrawableBackground(R.drawable.item_background);
        }
        titleText.setTextColor(ContextCompat.getColor(getContext(), color));
        dueDate.setTextColor(ContextCompat.getColor(getContext(), color));
        daysLeft.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public void showDetails(TaskItem item) {
        additional.setVisibility(View.VISIBLE);
        description.setText(item.getDescription());

        String statusText = item.getStatus().toString();
        if (StringUtils.isNotEmpty(item.getComment()))
            statusText += "\nComment: " + item.getComment();
        status.setText(statusText);
        setDrawableBackground(R.drawable.task_details);

        rootLayout.setPadding(10, 25, 10, rootLayout.getBottom());
    }

    void setDrawableBackground(int drawableInt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootLayout.setBackground(getContext().getDrawable(drawableInt));
        } else
            rootLayout.setBackgroundDrawable(getResources().getDrawable(drawableInt));
    }
}
