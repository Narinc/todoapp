package solidict.com.todoapp.data.source.local;

import android.support.annotation.NonNull;

import solidict.com.todoapp.data.source.TasksDataSource;
import solidict.com.todoapp.util.AppExecutors;

/**
 * Created by volkannarinc on 28.12.2017 18:26.
 */

public class LocalTasksDataSource implements TasksDataSource {

    private static volatile LocalTasksDataSource INSTANCE;

    private TaskDao taskDao;
    private AppExecutors appExecutors;

    public LocalTasksDataSource(TaskDao taskDao, AppExecutors appExecutors) {
        this.taskDao = taskDao;
        this.appExecutors = appExecutors;
    }

    public static LocalTasksDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TaskDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (LocalTasksDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalTasksDataSource(tasksDao, appExecutors);
                }
            }
        }
        return INSTANCE;
    }
}
