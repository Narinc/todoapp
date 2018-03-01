package solidict.com.todoapp.taskdetail;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.common.base.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import solidict.com.todoapp.R;
import solidict.com.todoapp.addedittask.AddEditTaskActivity;
import solidict.com.todoapp.addedittask.AddEditTaskFragment;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {

    @NonNull
    private static final String ARGUMENT_TASK_ID = "TASK_ID";

    @NonNull
    private static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter presenter;

    @BindView(R.id.task_detail_complete)
    CheckBox taskDetailComplete;
    @BindView(R.id.task_detail_title)
    TextView taskDetailTitle;
    @BindView(R.id.task_detail_description)
    TextView taskDetailDescription;
    Unbinder unbinder;

    public static TaskDetailFragment newInstance(@NonNull String taskId) {

        Bundle args = new Bundle();
        args.putString(ARGUMENT_TASK_ID, taskId);

        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        FloatingActionButton fab =
                getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editTask();
            }
        });

        return view;
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteTask();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            taskDetailTitle.setText("");
            taskDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingTask() {
        taskDetailTitle.setText("");
        taskDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void hideTitle() {
        taskDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        taskDetailTitle.setVisibility(View.VISIBLE);
        taskDetailTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        taskDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        taskDetailDescription.setVisibility(View.VISIBLE);
        taskDetailDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        Preconditions.checkNotNull(taskDetailComplete);

        taskDetailComplete.setChecked(complete);
        taskDetailComplete.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            presenter.completeTask();
                        } else {
                            presenter.activateTask();
                        }
                    }
                });
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
