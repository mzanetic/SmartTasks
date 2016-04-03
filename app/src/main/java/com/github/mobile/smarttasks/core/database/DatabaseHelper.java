package com.github.mobile.smarttasks.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.mobile.smarttasks.core.models.TaskItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getName();

    public static final  String DATABASE_NAME    = "appDB.db";
    private static final int    DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TaskItem.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not create new table: " + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, TaskItem.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Could not drop table: " + e);
        }
    }
}
