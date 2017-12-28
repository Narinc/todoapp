package solidict.com.todoapp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import solidict.com.todoapp.data.Task;

/**
 * Created by volkannarinc on 28.12.2017 18:12.
 */

@Database(entities = {Task.class}, version = 1)
public abstract class ToDoDatabase extends RoomDatabase {
    private static ToDoDatabase INSTANCE;

    public abstract TaskDao taskDao();

    private static final Object lock = new Object();

    public static ToDoDatabase getINSTANCE(Context context) {
        synchronized (lock) {
            if (context == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class, "Tasks.db")
                        .build();
            }

            return INSTANCE;
        }
    }
}
