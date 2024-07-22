package gay.nihil.lena.heartbeat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM user ORDER BY last_active_time")
    List<User> getUsers();

    @Query("SELECT * FROM user ORDER BY last_active_time")
    LiveData<List<User>> getUsersLive();

    @Query("SELECT * FROM user WHERE id = :uid")
    User getUser(long uid);

}