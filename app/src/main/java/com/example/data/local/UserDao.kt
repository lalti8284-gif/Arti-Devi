package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM sport_progress")
    fun getAllSportProgress(): Flow<List<SportProgressEntity>>

    @Query("SELECT * FROM sport_progress WHERE sportId = :sportId")
    suspend fun getSportProgress(sportId: String): SportProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSportProgress(progress: SportProgressEntity)

    @Query("SELECT * FROM daily_missions")
    fun getAllMissions(): Flow<List<DailyMissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<DailyMissionEntity>)

    @Update
    suspend fun updateMission(mission: DailyMissionEntity)

    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("SELECT * FROM practice_logs ORDER BY dateTimestamp DESC")
    fun getPracticeLogs(): Flow<List<PracticeLogEntity>>

    @Insert
    suspend fun insertPracticeLog(log: PracticeLogEntity)
}
