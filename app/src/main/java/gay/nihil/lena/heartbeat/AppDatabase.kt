package gay.nihil.lena.heartbeat

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Event::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?

    abstract fun eventDao(): EventDao?
}