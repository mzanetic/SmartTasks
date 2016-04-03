package com.github.mobile.smarttasks.core.database;

import android.content.Context;

import com.github.mobile.smarttasks.BuildConfig;
import com.github.mobile.smarttasks.core.content.ITasksController;
import com.github.mobile.smarttasks.core.content.RetrofitService;
import com.github.mobile.smarttasks.core.models.Status;
import com.github.mobile.smarttasks.core.models.TaskItem;
import com.github.mobile.smarttasks.core.models.TasksResponse;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DBManagerTest {

    Context               context;
    List<TaskItem>        items;
    DBManager             manager;
    DatabaseHelper        databaseHelper;
    Dao<TaskItem, String> taskItemDao;
    ITasksController      tasksController;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;

        manager = new DBManager();
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        taskItemDao = databaseHelper.getDao(TaskItem.class);
        manager.setTaskItemDao(taskItemDao);

        items = new ArrayList<>();

        DateFormat inputDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        Date parsedDate = inputDateFormat.parse(String.valueOf(calendar.getTime()));


        TaskItem item = new TaskItem("a1b1", "Do something", parsedDate, parsedDate, "This is a task", 1, Status.UNRESOLVED, "");
        items.add(item);

        item = new TaskItem("a1b2", "Do something else", parsedDate, parsedDate, "This is another task", 2, Status.RESOLVED, "Done");
        items.add(item);

        tasksController = RetrofitService.createService(ITasksController.class);
    }

//    @BeforeClass
//    public static void setup() {
//        // To redirect Robolectric to stdout
//        System.setProperty("robolectric.logging", "stdout");
//    }

    @Test
    public void testAddTasks() throws Exception {
        manager.addTasks(items);
        showAllValues();
    }


    private void showAllValues() throws SQLException {
        manager.printAll();
    }
}
