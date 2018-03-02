package solidict.com.todoapp.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.Iterator;
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
        addTask("Kitap özeti çıkarılacak", "Fahrenheit 421 kitap özeti yazılıp yorumlarla açıklanacak.");
        addTask("Android Architecture Components", "4 komponent birden kullanılarak sample bir proje oluşturulacak.");
    }

    public static TasksRemoteDataSource getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }

        return INSTANCE;
    }

    // Prevent direct instantiation.
    private TasksRemoteDataSource() {

    }

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    /**
     * Not: {@link GetTaskCallback#onDataNotAvailable()} metodu çağırılmaz. Gerçek bir
     * remote kullanılsa bir hata durumunda çağırılabilirdi.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Not: {@link GetTaskCallback#onDataNotAvailable()} metodu çağırılmaz. Gerçek bir
     * remote kullanılsa bir hata durumunda çağırılabilirdi.
     */
    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(task);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Remote data source için gerekli değildir, çünkü {@link TasksRepository}, önbelleğe alınan
        // verileri {@code taskId} kullanarak bir task' {@link Task} e dönüştürebilir.
    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Remote data source için gerekli değildir, çünkü {@link TasksRepository}, önbelleğe alınan
        // verileri {@code taskId} kullanarak bir task' {@link Task} e dönüştürebilir.
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Task>> iterator = TASKS_SERVICE_DATA.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> entry = iterator.next();
            if (entry.getValue().isCompleted()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        //Gerekli değil, çünkü görevler mevcut tüm datasource'lardan refresh mantığını işlemektedir.
    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }

    //private static void addTask(String title, String description) {
    //    Task newTask = new Task(title, description);
    //    TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    //}
}
