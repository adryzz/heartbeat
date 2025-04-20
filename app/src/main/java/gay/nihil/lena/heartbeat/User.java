package gay.nihil.lena.heartbeat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "last_active_time")
    public long last_active_time;

}
