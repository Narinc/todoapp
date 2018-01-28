package solidict.com.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;

import solidict.com.todoapp.data.FakeTasksRemoteDataSource;
import solidict.com.todoapp.data.source.TasksRepository;
import solidict.com.todoapp.data.source.local.TasksLocalDataSource;
import solidict.com.todoapp.data.source.local.ToDoDatabase;
import solidict.com.todoapp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by volkannarinc on 28.12.2017 12:07.
 */

public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);

        ToDoDatabase database = ToDoDatabase.getInstance(context);
        return TasksRepository.getINSTANCE(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.tasksDao()));
    }
}
