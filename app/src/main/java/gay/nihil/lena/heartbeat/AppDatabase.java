package gay.nihil.lena.heartbeat;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Event.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract EventDao eventDao();
}