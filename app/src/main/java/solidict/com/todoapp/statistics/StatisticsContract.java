package solidict.com.todoapp.statistics;

import solidict.com.todoapp.BasePresenter;
import solidict.com.todoapp.BaseView;

/**
 * Created by volkannarinc on 5.03.2018 11:21.
 */

public interface StatisticsContract {

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}
