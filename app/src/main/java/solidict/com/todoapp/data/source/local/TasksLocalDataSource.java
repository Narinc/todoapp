package solidict.com.todoapp.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;

import java.util.List;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;
import solidict.com.todoapp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by volkannarinc on 28.12.2017 18:26.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TasksDao tasksDao;

    private AppExecutors appExecutors;

    public TasksLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull TasksDao tasksDao) {
        this.appExecutors = appExecutors;
        this.tasksDao = tasksDao;
    }

    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = tasksDao.getTasks();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else callback.onTasksLoaded(tasks);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = tasksDao.getTaskById(taskId);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            callback.onTaskLoaded(task);
                        } else callback.onDataNotAvailable();
                    }
                });
            }
        };
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.insertTask(task);
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void completeTask(@NonNull final Task task) {
        checkNotNull(task);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.updateComplated(task.getId(), true);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Lokalda yapmaya gerek yok. Çünkü {@link TasksRepository} , önbellek verilerini kullanarak
        // bir {@code taskId}'yi {@link task} objesine dönüştürebilir.
    }

    @Override
    public void activateTask(@NonNull final Task task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.updateComplated(task.getId(), false);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Lokalda yapmaya gerek yok. Çünkü {@link TasksRepository} , önbellek verilerini kullanarak
        // bir {@code taskId}'yi {@link task} objesine dönüştürebilir.
    }

    @Override
    public void clearCompletedTasks() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.deleteTasks();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshTasks() {
        // Gerekli değil çünkü {@link TasksRepository}, mevcut tüm veri kaynaklarından gelen task'leri yenileme mantığını işlemektedir.
    }

    @Override
    public void deleteAllTasks() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.deleteTasks();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteTask(@NonNull final String taskId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.deleteTaskById(taskId);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }
}
