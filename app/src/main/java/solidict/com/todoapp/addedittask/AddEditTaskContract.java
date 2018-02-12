package solidict.com.todoapp.addedittask;

import solidict.com.todoapp.BasePresenter;
import solidict.com.todoapp.BaseView;

/**
 * Created by volkan on 13.02.2018 00:03.
 */

public interface AddEditTaskContract {
    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}
