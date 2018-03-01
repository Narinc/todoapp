package solidict.com.todoapp.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;

/**
 * Created by volkan on 13.02.2018 00:02.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter,
        TasksDataSource.GetTaskCallback {

    @NonNull
    private final TasksDataSource taskRepository;

    @NonNull
    private final AddEditTaskContract.View view;

    @Nullable
    private String taskId;

    private boolean isDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param taskId                 ID of the task to edit or null for a new task
     * @param taskRepository         a repository of data for tasks
     * @param view                   the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditTaskPresenter(@NonNull TasksDataSource taskRepository,
                                @NonNull AddEditTaskContract.View view,
                                @Nullable String taskId, boolean shouldLoadDataFromRepo) {
        this.taskRepository = taskRepository;
        this.view = view;
        this.taskId = taskId;
        this.isDataMissing = shouldLoadDataFromRepo;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask() && isDataMissing) {
            populateTask();
        }
    }

    private boolean isNewTask() {
        return taskId == null;
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (view.isActive()) {
            view.setTitle(task.getTitle());
            view.setDescription(task.getDescription());
        }
        isDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if (view.isActive()) {
            view.showEmptyTaskError();
        }
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new");
        }

        taskRepository.saveTask(new Task(title, description, taskId));
        view.showTasksList(); // After an edit, go back to the list.
    }

    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);

        if (newTask.isEmpty()) {
            view.showEmptyTaskError();
        } else {
            taskRepository.saveTask(newTask);
            view.showTasksList();
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new");
        }
        taskRepository.getTask(taskId, this);
    }

    @Override
    public boolean isDataMissing() {
        return isDataMissing;
    }
}
