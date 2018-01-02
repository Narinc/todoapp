package solidict.com.todoapp.data.source.local;

import android.support.annotation.NonNull;

import solidict.com.todoapp.data.source.TasksDataSource;
import solidict.com.todoapp.util.AppExecutors;

/**
 * Created by volkannarinc on 28.12.2017 18:26.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TaskDao taskDao;
    private AppExecutors appExecutors;

    public TasksLocalDataSource(TaskDao taskDao, AppExecutors appExecutors) {
        this.taskDao = taskDao;
        this.appExecutors = appExecutors;
    }

    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TaskDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(tasksDao, appExecutors);
                }
            }
        }
        return INSTANCE;
    }
}
