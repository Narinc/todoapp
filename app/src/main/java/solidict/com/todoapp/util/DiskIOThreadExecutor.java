package solidict.com.todoapp.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by volkannarinc on 29.12.2017 12:17.
 */

public class DiskIOThreadExecutor implements Executor {
    private final Executor diskIO;

    public DiskIOThreadExecutor() {
        diskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        diskIO.execute(runnable);
    }
}
