package com.github.mobile.smarttasks.core.content;

import android.content.Context;

import com.github.mobile.smarttasks.BuildConfig;
import com.github.mobile.smarttasks.core.models.Status;
import com.github.mobile.smarttasks.core.models.TaskItem;
import com.github.mobile.smarttasks.core.models.TasksResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ITasksControllerTest {

    ITasksController tasksController;
    Context          context;
    TaskItem         item;


    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        tasksController = RetrofitService.createService(ITasksController.class);
    }

    @Test
    public void testGetTasks() throws Exception {

        TasksResponse response = tasksController.getTasks().execute().body();

        List<TaskItem> items = response.getTasks();

        assertNotNull(items);

        item = items.get(0);

        assertNotNull(item);
    }

    @Test
    public void testItemValues() throws Exception {
        TasksResponse response = tasksController.getTasks().execute().body();

        item = response.getTasks().get(0);

        assertThat(item.getId()).isEqualTo("83f2027d572645d296e9a6330a376ac7");
        assertThat(item.getStatus()).isEqualTo(Status.UNRESOLVED);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getDueDate());

        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(28);

    }
}
