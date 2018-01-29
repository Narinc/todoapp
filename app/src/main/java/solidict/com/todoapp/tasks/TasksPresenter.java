package solidict.com.todoapp.tasks;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.data.source.TasksDataSource;
import solidict.com.todoapp.data.source.TasksRepository;
import solidict.com.todoapp.util.EspressoIdlingResource;

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

        tasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // TODO: 28.01.2018
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Örneklemin basitleştirilmesi: İlk yüklemede yeniden yüklenmeye zorlanacaktır.
        loadTasks(forceUpdate || firstLoad, true);
        firstLoad = false; // artık ilk yükleme modunda değil.
    }

    /**
     * @param forceUpdate   eğer 'true' olursa veriyi {@link TasksDataSource}' tan yenile
     * @param showLoadingUI eğer 'true' olursa loading ikonunu göster
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            tasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            tasksRepository.refreshTasks();
        }

        //Request farklı bir thread içinde işlenebilir, bu nedenle Espresso,
        //response gelinceye kadar uygulamanın meşgul olduğunu bildiğinizden emin olun.
        // TODO: 29.01.2018 incelenecek
        EspressoIdlingResource.increment(); /* Uygulama, bir sonraki ikaza kadar meşgul*/

        tasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> taskToShow = new ArrayList<>();

                //Bu callback, bir kez önbellek için ve birkez de sunucu API'sından
                // veri yüklemek için iki kez çağrılabilir, böylece decrement'ten
                // önce kontrol edilir, aksi halde "Counter has been corrupted!" istisnası döndürür.
                // TODO: 29.01.2018 incelenecek.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }

                for (Task task : tasks) {
                    switch (currentFilterType) {
                        case ALL_TASKS:
                            taskToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                taskToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                taskToShow.add(task);
                            }
                            break;
                        default:
                            taskToShow.add(task);
                            break;
                    }
                }
                if (!tasksView.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    tasksView.setLoadingIndicator(false);
                }

                processTasks(taskToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!tasksView.isActive()) {
                    return;
                }
                tasksView.showLoadingTasksError();
            }
        });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // İlgili filtre tipi ile ilgili yapılacak olmadığını göster
            processEmptyTasks();
        } else {
            // Yapılacaklar listesini göster
            tasksView.showTasks(tasks);
            // Filtre tipi yazısını belirle
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (currentFilterType) {
            case ACTIVE_TASKS:
                tasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                tasksView.showCompletedFilterLabel();
                break;
            default:
                tasksView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (currentFilterType) {
            case ACTIVE_TASKS:
                tasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                tasksView.showNoCompletedTasks();
                break;
            default:
                tasksView.showNoTasks();
                break;
        }
    }


    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {

    }

    @Override
    public void completeTask(@NonNull Task completedTask) {

    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void setFiltering(TasksFilterType requestType) {

    }

    @Override
    public TasksFilterType getFiltering() {
        return null;
    }
}
