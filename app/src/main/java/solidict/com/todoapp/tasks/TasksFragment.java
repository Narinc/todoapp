package solidict.com.todoapp.tasks;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import solidict.com.todoapp.R;
import solidict.com.todoapp.addedittask.AddEditTaskActivity;
import solidict.com.todoapp.data.Task;
import solidict.com.todoapp.taskdetail.TaskDetailActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements TasksContract.View {

    @BindView(R.id.filteringLabel)
    TextView filteringLabel;
    @BindView(R.id.tasks_list)
    ListView tasksList;
    @BindView(R.id.ll_tasks)
    LinearLayout llTasks;
    @BindView(R.id.noTasksIcon)
    ImageView noTasksIcon;
    @BindView(R.id.noTasksMain)
    TextView noTasksMain;
    @BindView(R.id.noTasksAdd)
    TextView noTasksAdd;
    @BindView(R.id.noTasks)
    LinearLayout noTasks;
    @BindView(R.id.tasksContainer)
    RelativeLayout tasksContainer;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    private TasksContract.Presenter presenter;

    private TasksAdapter tasksAdapter;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasksAdapter = new TasksAdapter(new ArrayList<Task>(0), taskItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        unbinder = ButterKnife.bind(this, root);

        tasksList.setAdapter(tasksAdapter);
        noTasksAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTask();
            }
        });

        FloatingActionButton fab =
                getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewTask();
            }
        });

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        refreshLayout.setScrollUpChild(tasksList);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadTasks(false);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                presenter.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                presenter.loadTasks(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }


    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        presenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        presenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        presenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
                presenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    TaskItemListener taskItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            presenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            presenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            presenter.activateTask(activatedTask);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasks(List<Task> tasks) {
        tasksAdapter.replaceData(tasks);

        llTasks.setVisibility(View.VISIBLE);
        noTasks.setVisibility(View.GONE);
    }


    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        llTasks.setVisibility(View.GONE);
        noTasks.setVisibility(View.VISIBLE);

        noTasksMain.setText(mainText);
        noTasksIcon.setImageDrawable(getResources().getDrawable(iconRes));
        noTasksAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showActiveFilterLabel() {
        filteringLabel.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        filteringLabel.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabel.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
