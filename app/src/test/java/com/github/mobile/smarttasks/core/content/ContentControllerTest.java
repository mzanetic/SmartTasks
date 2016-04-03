package com.github.mobile.smarttasks.core.content;

import android.content.Context;

import com.github.mobile.smarttasks.BuildConfig;
import com.github.mobile.smarttasks.core.models.TaskItem;
import com.github.mobile.smarttasks.core.models.TasksResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ContentControllerTest {

    ITasksController  tasksController;
    Context           context;
    TasksResponse     response;
    String            dayKey;
    List<TaskItem>    items;
    ContentController controller;


    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        tasksController = RetrofitService.createService(ITasksController.class);
        response = tasksController.getTasks().execute().body();
        dayKey = "01022016";
    }

    @Test
    public void testMapEntries() throws Exception {
        controller = new ContentController();
        controller.setDateMap(new HashMap<String, List<TaskItem>>());
        controller.addToMap(response.getTasks());

        HashMap<String, List<TaskItem>> resultMap = controller.getDateMap();
        Iterator it = resultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
//            it.remove();
        }

        assertNotNull(resultMap);
        items = resultMap.get(dayKey);
        assertThat(resultMap.get(dayKey).size()).isEqualTo(6);

        controller.sortTasks(dayKey, items);
        List<TaskItem> list = controller.getDayItems(dayKey);

        for (TaskItem item : list) {
            System.out.println(item);
        }
    }

//    @Test
//    public void testSorting() throws Exception {
//        assertNotNull(items);
//        controller.sortTasks(dayKey, items);
//        List<TaskItem> list = controller.getDayItems(dayKey);
//
//        for (TaskItem item: list){
//            System.out.println(item);
//        }
//    }
}
