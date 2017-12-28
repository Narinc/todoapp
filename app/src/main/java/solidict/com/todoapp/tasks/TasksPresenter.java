package solidict.com.todoapp.tasks;

import solidict.com.todoapp.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by volkannarinc on 28.12.2017 17:41.
 */

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository tasksRepository;
    private final TasksContract.View tasksView;
    private TasksFilterType currentFilterType = TasksFilterType.ALL_TASKS;
    private boolean firstLoad = true;

    public TasksPresenter(TasksRepository tasksRepository, TasksContract.View tasksView) {
        this.tasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        this.tasksView = checkNotNull(tasksView, "tasksView cannot be null!");
    }

    @Override
    public void start() {

    }

    @Override
    public void setFiltering(TasksFilterType currentFiltering) {

    }
}
