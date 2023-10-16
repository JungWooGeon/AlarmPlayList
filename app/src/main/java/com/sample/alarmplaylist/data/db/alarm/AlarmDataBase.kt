package com.sample.alarmplaylist.data.db.alarm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.data.entity.Alarm

@Database(entities = [Alarm::class], version = 3)
abstract class AlarmDataBase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmDataBase? = null

        fun getInstance(context: Context): AlarmDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmDataBase::class.java, Constants.ALARM_DB
            )
            .addMigrations(MIGRATION_2_3)
            .build()

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL("""
            CREATE TABLE Alarm_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                alarmHour TEXT NOT NULL,
                alarmMinute TEXT NOT NULL,
                onOff INTEGER NOT NULL DEFAULT 0,
                playlistId INTEGER NOT NULL,
                playlistName TEXT NOT NULL
            )
        """)

                // Copy the data
                database.execSQL("""
            INSERT INTO Alarm_new (id, alarmHour, alarmMinute, onOff, playlistId, playlistName)
            SELECT id, alarmHour, alarmMinute,CASE WHEN onOff > 0 THEN 1 ELSE 0 END AS onOff ,playlistId ,playlistName FROM Alarm
        """)

                // Remove the old table
                database.execSQL("DROP TABLE Alarm")

                // Change the table name to the correct one
                database.execSQL("ALTER TABLE Alarm_new RENAME TO Alarm")
            }
        }
    }
}