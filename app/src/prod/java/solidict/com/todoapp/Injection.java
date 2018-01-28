package solidict.com.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;

import solidict.com.todoapp.data.source.TasksRepository;
import solidict.com.todoapp.data.source.local.TasksLocalDataSource;
import solidict.com.todoapp.data.source.remote.TasksRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by volkannarinc on 29.12.2017 15:25.
 */

public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);

        return TasksRepository.getINSTANCE(TasksRemoteDataSource.getINSTANCE(),
                TasksLocalDataSource.getInstance(context));
    }
}
