package solidict.com.todoapp.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (cachedTasks != null && !cacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<Task>(cachedTasks.values()));
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            localTasksDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    //Artık önbellekteki yapılacak işler güncellendi.
                    callback.onTasksLoaded(new ArrayList<Task>(cachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void refreshCache(List<Task> tasks) {
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.clear();
        for (Task task : tasks) {
            cachedTasks.put(task.getId(), task);
        }
        cacheIsDirty = false;
    }

    private void getTasksFromRemoteDataSource(final LoadTasksCallback callback) {
        remoteTasksDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<Task>(cachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        localTasksDataSource.deleteAllTasks();
        for (Task task : tasks) {
            localTasksDataSource.saveTask(task);
        }
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
