package com.github.mobile.smarttasks.android.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.mobile.smarttasks.android.ui.TaskItemView;
import com.github.mobile.smarttasks.android.ui.TaskItemView_;
import com.github.mobile.smarttasks.core.models.TaskItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

@EBean
public class TasksListAdapter extends BaseAdapter {

    List<TaskItem> taskItems;

    @RootContext
    Context context;


    @AfterInject
    void init() {
        taskItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public TaskItem getItem(int position) {
        return taskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItemView itemView;

        if (convertView == null)
            itemView = TaskItemView_.build(context);
        else itemView = (TaskItemView) convertView;

        if (getItem(position) != null) {
            itemView.bind(getItem(position));
            itemView.applyTheme(getItem(position).getStatus());
        }
        return itemView;
    }

    @UiThread
    public void setTaskItems(List<TaskItem> taskItems) {
        this.taskItems = taskItems;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 300);
    }


}
