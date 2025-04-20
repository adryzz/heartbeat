package gay.nihil.lena.heartbeat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Insert
    fun insert(event: Event)

    @Update
    fun update(event: Event)

    @Query("SELECT * FROM event ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEventsLive(limit: Int): LiveData<MutableList<Event>>

    @Query("SELECT * FROM event ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEvents(limit: Int): MutableList<Event>
}