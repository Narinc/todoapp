package solidict.com.todoapp.data.source.remote;

import java.util.LinkedHashMap;
import java.util.Map;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;

/**
 * Created by volkannarinc on 29.12.2017 15:17.
 */

public class TasksRemoteDataSource implements TasksDataSource {
    private static TasksRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Task> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        //addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        //addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static TasksRemoteDataSource getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }

        return INSTANCE;
    }

    private TasksRemoteDataSource() {

    }

    //private static void addTask(String title, String description) {
    //    Task newTask = new Task(title, description);
    //    TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    //}
}
