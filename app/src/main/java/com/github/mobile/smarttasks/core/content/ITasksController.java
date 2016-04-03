package com.github.mobile.smarttasks.core.content;

import com.github.mobile.smarttasks.core.models.TasksResponse;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ITasksController {

    @GET("tasks")
    Call<TasksResponse> getTasks();

}
