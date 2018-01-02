package solidict.com.todoapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by volkannarinc on 28.12.2017 17:52.
 */

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    public Task(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
