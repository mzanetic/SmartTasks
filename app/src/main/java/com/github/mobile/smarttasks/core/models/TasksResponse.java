package com.github.mobile.smarttasks.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TasksResponse {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("tasks ")
    @Expose
    private List<TaskItem> tasks = new ArrayList<TaskItem>();

    public Integer getStatus() {
        return status;
    }

    public List<TaskItem> getTasks() {
        return tasks;
    }
}
