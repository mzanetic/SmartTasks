package com.github.mobile.smarttasks.core.database;

import android.util.Log;

import com.github.mobile.smarttasks.core.content.ContentController;
import com.github.mobile.smarttasks.core.models.TaskItem;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Callable;

@EBean(scope = EBean.Scope.Singleton)
public class DBManager {

    private static final String LOG_TAG = DBManager.class.getName();

    @OrmLiteDao(helper = DatabaseHelper.class, model = TaskItem.class)
    Dao<TaskItem, String> taskItemDao;

    public void addTasks(final List<TaskItem> items) throws Exception {
        taskItemDao.callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws SQLException {
                for (TaskItem item : items) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ContentController.MAP_DATE_FORMAT);
                    item.setPageKey(simpleDateFormat.format(item.getTargetDate()));
                    taskItemDao.create(item);
                }
                return null;
            }
        });
    }

    public int getTaskCount() throws SQLException {
        return (int) taskItemDao.countOf();
    }

    public void printAll() throws SQLException {
        List<TaskItem> items = taskItemDao.queryForAll();
        for (TaskItem item : items) {
            Log.i(LOG_TAG, item.toString());
            System.out.println(item.toString());
        }
    }

    public void setTaskItemDao(Dao<TaskItem, String> taskItemDao) {
        this.taskItemDao = taskItemDao;
    }

    public List<TaskItem> getAllTasks() throws SQLException {
        return taskItemDao.queryForAll();
    }

    public void updateTask(TaskItem taskItem) throws SQLException {
        taskItemDao.update(taskItem);
    }

    public List<TaskItem> queryForDayTasks(String key) throws SQLException {
        return taskItemDao.queryForEq("pageKey", key);
    }
}
