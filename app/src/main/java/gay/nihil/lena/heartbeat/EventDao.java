package gay.nihil.lena.heartbeat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Query("SELECT * FROM event ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<Event>> getRecentEventsLive(int limit);

    @Query("SELECT * FROM event ORDER BY timestamp DESC LIMIT :limit")
    List<Event> getRecentEvents(int limit);

}