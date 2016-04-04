package com.github.mobile.smarttasks.core.content;

import android.util.Log;

import com.github.mobile.smarttasks.core.database.DBManager;
import com.github.mobile.smarttasks.core.models.Status;
import com.github.mobile.smarttasks.core.models.TaskItem;
import com.github.mobile.smarttasks.core.models.TasksResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Class containing tasks loaded in a map with a pageKey as their key value.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ContentController {

    private static final String LOG_TAG = ContentController.class.getName();

    // Format for the displayed dates
    public static final String DATE_FORMAT = "d MMM yyyy";

    // Format for the pageKey value, each pageKey corresponds to one calendar day
    public static final String MAP_DATE_FORMAT = "ddMMyyyy";

    private List<TaskItem> taskItems;

    // Map containing tasks, String is the pageKey taskItem value
    private HashMap<String, List<TaskItem>> dateMap;

    @Bean
    DBManager persistence;

    @Bean
    RetrofitService retrofitService;

    ITasksController tasksController;

    @AfterInject
    void init() {
        tasksController = retrofitService.getTasksController();
        dateMap = new HashMap<>();
    }

    /**
     * Used to load the tasks and store them in the database
     *
     * @throws Exception
     */
    public void getAndStoreTasks() throws Exception {
        TasksResponse response = tasksController.getTasks().execute().body();
        taskItems = response.getTasks();
        addToMap(taskItems);
        persistence.addTasks(taskItems);
    }

    public void loadExistingData() throws SQLException {
        taskItems = persistence.getAllTasks();
        addToMap(taskItems);
    }

    public void addToMap(List<TaskItem> items) {
        for (TaskItem item : items) {
            Date date = item.getTargetDate();
            SimpleDateFormat format = new SimpleDateFormat(MAP_DATE_FORMAT, Locale.ENGLISH);
            String key = format.format(date);
            if (dateMap.containsKey(key)) {
                List<TaskItem> list = dateMap.get(key);
                list.add(item);
            } else {
                List<TaskItem> list = new ArrayList<>();
                list.add(item);
                dateMap.put(key, list);
            }
        }
    }

    /**
     * Sorts tasks based on status, priority and date.
     * Tasks with resolved status will always be shown below the unresolved ones.
     * In case two tasks are the same priority, older one comes first.
     *
     * @param key   Day map key
     * @param items List to be sorted
     * @return Sorted list of task items
     */
    List<TaskItem> sortTasks(String key, List<TaskItem> items) {
        List<TaskItem> statusItems = new ArrayList<>();
        List<TaskItem> unresolved = new ArrayList<>();

        if (items == null) return null;
        for (TaskItem item : items) {
            if (item.getStatus().equals(Status.UNRESOLVED)) unresolved.add(item);
            else statusItems.add(item);
        }

        Comparator<TaskItem> comparator = new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem taskOne, TaskItem taskTwo) {
                int priorityCompare = newApiCompare(taskOne.getPriority(), taskTwo.getPriority());

                // for the same priority, compare dates
                if (priorityCompare == 0) {
                    return taskOne.getTargetDate().compareTo(taskTwo.getTargetDate());
                } else return priorityCompare;
            }
        };

        Collections.sort(unresolved, comparator);

        unresolved.addAll(statusItems);
        dateMap.put(key, unresolved);
        return unresolved;
    }

    /**
     * Will retrieve all the database entries for the specified key value
     *
     * @param key Day's date value formatted with MAP_DATE_FORMAT
     * @return List of TaskItems from dateMap
     */
    public List<TaskItem> getDayItems(String key) {
        List<TaskItem> input = dateMap.get(key);
        return sortTasks(key, input);
    }

    public void setDateMap(HashMap<String, List<TaskItem>> dateMap) {
        this.dateMap = dateMap;
    }

    public HashMap<String, List<TaskItem>> getDateMap() {
        return dateMap;
    }

    private static int newApiCompare(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    /**
     * Updates the task item within the dateMap and updates the task in the database
     *
     * @param taskItem
     */
    public void updateTask(TaskItem taskItem) {
        List<TaskItem> items = dateMap.get(taskItem.getPageKey());
        if (items.contains(taskItem)) {
            int position = items.indexOf(taskItem);
            taskItems.remove(position);
            taskItems.add(position, taskItem);
        }

        try {
            persistence.updateTask(taskItem);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Cannot update task in database: " + e.getLocalizedMessage());
        }

    }

    /**
     * Will clear all all entries with the key and fetch new ones from the database.
     * After the database loading, this method will also sort.
     *
     * @param key
     */
    public void refreshDay(String key) {
        dateMap.get(key).clear();
        List<TaskItem> dayItems = null;
        try {
            dayItems = persistence.queryForDayTasks(key);
            sortTasks(key, dayItems);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Cannot retrieve from database: " + e.getLocalizedMessage());
        }

    }
}
