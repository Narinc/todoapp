package solidict.com.todoapp.addedittask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import solidict.com.todoapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditTaskFragment extends Fragment {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    public static AddEditTaskFragment newInstance() {
        AddEditTaskFragment fragment = new AddEditTaskFragment();
        return fragment;
    }

    public AddEditTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_task, container, false);
    }

}
