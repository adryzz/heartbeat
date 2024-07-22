package gay.nihil.lena.heartbeat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "user_id")
    public long user;

    @ColumnInfo(name = "flags")
    public int flags;
}
