package solidict.com.todoapp.tasks;

import solidict.com.todoapp.data.Task;

/**
 * Created by volkan on 28.01.2018 02:05.
 */

public interface TaskItemListener {

    void onTaskClick(Task clickedTask);

    void onCompleteTaskClick(Task completedTask);

    void onActivateTaskClick(Task activatedTask);
}
