package solidict.com.todoapp.taskdetail;

import solidict.com.todoapp.BasePresenter;
import solidict.com.todoapp.BaseView;

/**
 * Created by volkan on 01.03.2018 23:10.
 */

public interface TaskDetailContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }
}
