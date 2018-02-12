package solidict.com.todoapp.addedittask;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;

/**
 * Created by volkan on 13.02.2018 00:02.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter,
        TasksDataSource.GetTaskCallback{
    @Override
    public void start() {

    }

    @Override
    public void onTaskLoaded(Task task) {

    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void saveTask(String title, String description) {

    }

    @Override
    public void populateTask() {

    }

    @Override
    public boolean isDataMissing() {
        return false;
    }
}
