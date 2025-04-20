package gay.nihil.lena.heartbeat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM User ORDER BY last_active_time")
    fun getUsers(): MutableList<User>

    @Query("SELECT * FROM user ORDER BY last_active_time")
    fun getUsersLive(): LiveData<MutableList<User>>

    @Query("SELECT * FROM user WHERE id = :uid")
    fun getUser(uid: Long): User?
}