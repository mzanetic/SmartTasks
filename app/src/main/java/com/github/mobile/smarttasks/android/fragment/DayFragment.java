package com.github.mobile.smarttasks.android.fragment;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mobile.smarttasks.R;
import com.github.mobile.smarttasks.android.activity.DetailsActivity;
import com.github.mobile.smarttasks.android.activity.DetailsActivity_;
import com.github.mobile.smarttasks.android.ui.adapter.TasksListAdapter;
import com.github.mobile.smarttasks.core.content.ContentController;
import com.github.mobile.smarttasks.core.models.TaskItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.day_fragment)
public class DayFragment extends Fragment {

    private static final String LOG_TAG        = DayFragment.class.getName();
    public static final  String REFRESH_ACTION = "action:refresh";

    private String dateKeyString;

    @ViewById(R.id.item_list)
    ListView listView;

    @ViewById(R.id.empty_screen_layout)
    LinearLayout emptyScreen;

    @Bean
    TasksListAdapter adapter;

    @Bean
    ContentController controller;

    @AfterViews
    void init() {
        listView.setAdapter(adapter);
        List<TaskItem> todayItems = controller.getDayItems(dateKeyString);
        Log.i(LOG_TAG, dateKeyString);
        if (todayItems != null && todayItems.size() > 0)
            adapter.setTaskItems(todayItems);
        else {
            listView.setVisibility(View.GONE);
            emptyScreen.setVisibility(View.VISIBLE);
        }
    }

    @ItemClick(R.id.item_list)
    void listItemClicked(TaskItem item) {
        DetailsActivity_.intent(getActivity()).taskItem(item).startForResult(DetailsActivity.REQUEST_CODE);
    }

    public void setDateKeyString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(ContentController.MAP_DATE_FORMAT, Locale.ENGLISH);
        dateKeyString = format.format(date);
    }

    @UiThread
    public void refreshData() {
        List<TaskItem> todayItems = controller.getDayItems(dateKeyString);
        adapter.setTaskItems(todayItems);
    }

    @Receiver(actions = DayFragment.REFRESH_ACTION)
    protected void onReceiveAction() {
        refreshData();
    }
}