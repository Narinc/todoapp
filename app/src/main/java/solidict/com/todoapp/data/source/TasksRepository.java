package solidict.com.todoapp.data.source;

import android.support.annotation.NonNull;

import java.util.Map;

import solidict.com.todoapp.data.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by volkannarinc on 28.12.2017 17:58.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;
    private final TasksDataSource remoteTasksDataSource;
    private final TasksDataSource localTasksDataSource;

    Map<String, Task> cachedTasks;

    boolean cacheIsDirty = false;

    public TasksRepository(TasksDataSource remoteTasksDataSource, TasksDataSource localTasksDataSource) {
        this.remoteTasksDataSource = checkNotNull(remoteTasksDataSource);
        this.localTasksDataSource = checkNotNull(localTasksDataSource);
    }

    public static TasksRepository getINSTANCE(TasksDataSource remoteTasksDataSource,
                                              TasksDataSource localTasksDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(remoteTasksDataSource, localTasksDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
