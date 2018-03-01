package solidict.com.todoapp.taskdetail;

import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;
import solidict.com.todoapp.data.source.TasksRepository;

/**
 * Created by volkan on 01.03.2018 23:30.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private final TasksRepository tasksRepository;
    private final TaskDetailContract.View view;

    @Nullable
    private String taskId;

    public TaskDetailPresenter(TasksRepository tasksRepository, TaskDetailContract.View view, String taskId) {
        this.tasksRepository = tasksRepository;
        this.view = view;
        this.taskId = taskId;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            view.showMissingTask();
            return;
        }

        view.setLoadingIndicator(true);
        tasksRepository.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!view.isActive()) {
                    return;
                }

                view.setLoadingIndicator(false);
                if (task == null) {
                    view.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!view.isActive()) {
                    return;
                }
                view.showMissingTask();
            }
        });
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            view.hideTitle();
        } else {
            view.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            view.hideDescription();
        } else {
            view.showDescription(description);
        }
        view.showCompletionStatus(task.isCompleted());
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            view.showMissingTask();
            return;
        }
        view.showEditTask(taskId);
    }

    @Override
    public void deleteTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            view.showMissingTask();
            return;
        }

        tasksRepository.deleteTask(taskId);
        view.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            view.showMissingTask();
            return;
        }
        tasksRepository.completeTask(taskId);
        view.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (Strings.isNullOrEmpty(taskId)) {
            view.showMissingTask();
            return;
        }
        tasksRepository.activateTask(taskId);
        view.showTaskMarkedActive();
    }
}
