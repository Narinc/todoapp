package solidict.com.todoapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
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

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetTaskCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        // Respond immediately with cache if available
        // Önbellekte boş değilse hemen bir cevap döner.
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.
        // Gerekirse kalıcı bir şekilde/server'dan yükle

        // Is the task in the local data source? If not, query the network.
        // Yapılacak iş yerel veri kaynağında var mı? Yoksa, request at.
        localTasksDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                // Do in memory cache update to keep the app UI up to date
                // Kullanıcı arayüzünü güncel tutmak için önbellek güncellenir.
                if (cachedTasks == null) {
                    cachedTasks = new LinkedHashMap<>();
                }
                cachedTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                remoteTasksDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        // Do in memory cache update to keep the app UI up to date
                        // Kullanıcı arayüzünü güncel tutmak için önbellek güncellenir.
                        if (cachedTasks == null) {
                            cachedTasks = new LinkedHashMap<>();
                        }
                        cachedTasks.put(task.getId(), task);
                        callback.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Nullable
    private Task getTaskWithId(@Nullable String id) {
        checkNotNull(id);

        if (cachedTasks == null || cachedTasks.isEmpty()) {
            return null;
        } else return cachedTasks.get(id);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);

        remoteTasksDataSource.saveTask(task);
        localTasksDataSource.saveTask(task);

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);

        localTasksDataSource.completeTask(task);
        remoteTasksDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);

        localTasksDataSource.activateTask(task);
        remoteTasksDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        localTasksDataSource.clearCompletedTasks();
        remoteTasksDataSource.clearCompletedTasks();

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }

        Iterator<Map.Entry<String, Task>> entryIterator = cachedTasks.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Task> entry = entryIterator.next();
            if (entry.getValue().isCompleted()) {
                entryIterator.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        localTasksDataSource.deleteAllTasks();
        remoteTasksDataSource.deleteAllTasks();

        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        localTasksDataSource.deleteTask(taskId);
        remoteTasksDataSource.deleteTask(taskId);

        cachedTasks.remove(taskId);
    }
}
