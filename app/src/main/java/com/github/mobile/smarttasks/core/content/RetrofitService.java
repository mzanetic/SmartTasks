package com.github.mobile.smarttasks.core.content;

import com.github.mobile.smarttasks.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class RetrofitService {

    private static final String BASE_URL = BuildConfig.ROOT_URL;

    private static OkHttpClient httpClient = new OkHttpClient();

    ITasksController tasksController;

    @AfterInject
    void init() {
        tasksController = createService(ITasksController.class);
    }

    public static <S> S createService(Class<S> serviceClass) {

        // GsonBuilder for the input date format
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public ITasksController getTasksController() {
        return tasksController;
    }
}
