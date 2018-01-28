package solidict.com.todoapp.tasks;

import solidict.com.todoapp.BasePresenter;
import solidict.com.todoapp.BaseView;

/**
 * Created by volkannarinc on 28.12.2017 17:49.
 */

public interface TasksContract {

    interface View extends BaseView<Presenter> {
        void showFilteringPopUpMenu();

    }

    interface Presenter extends BasePresenter {

        void setFiltering(TasksFilterType currentFiltering);

        TasksFilterType getFiltering();

    }
}
