package com.sample.alarmplaylist.data.db.playlist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.data.entity.Playlist

@Database(entities = [Playlist::class], version = 2)
abstract class PlaylistDataBase : RoomDatabase() {
    abstract fun playListDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: PlaylistDataBase? = null

        fun getInstance(context: Context): PlaylistDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PlaylistDataBase::class.java, Constants.PLAYLIST_DB
            ).addMigrations(MIGRATION_1_2).build()

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    """
            CREATE TABLE Playlist_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                playListTitle TEXT NOT NULL
            )
        """
                )

                // Copy the data
                database.execSQL(
                    """
            INSERT INTO Playlist_new (id, playListTitle)
            SELECT id, playListTitle FROM Playlist
        """
                )

                // Remove the old table
                database.execSQL("DROP TABLE Playlist")

                // Change the table name to the correct one
                database.execSQL("ALTER TABLE Playlist_new RENAME TO Playlist")
            }
        }
    }
}