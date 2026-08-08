package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        SportProgressEntity::class,
        DailyMissionEntity::class,
        AchievementEntity::class,
        PracticeLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SportsVerseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: SportsVerseDatabase? = null

        fun getDatabase(context: Context): SportsVerseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SportsVerseDatabase::class.java,
                    "sportsverse_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
