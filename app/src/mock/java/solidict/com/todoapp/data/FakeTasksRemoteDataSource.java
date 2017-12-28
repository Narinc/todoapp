package solidict.com.todoapp.data;

import java.util.LinkedHashMap;
import java.util.Map;

import solidict.com.todoapp.data.source.TasksDataSource;

/**
 * Created by volkannarinc on 28.12.2017 12:08.
 */

public class FakeTasksRemoteDataSource implements TasksDataSource {
    private static FakeTasksRemoteDataSource INSTANCE;
    private static final Map<String, Task> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeTasksRemoteDataSource() {
    }

    public static FakeTasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeTasksRemoteDataSource();
        }
        return INSTANCE;
    }

}
