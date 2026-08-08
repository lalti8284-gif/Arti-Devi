package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Champion_Player",
    val age: Int = 16,
    val gender: String = "Male",
    val state: String = "Maharashtra",
    val school: String = "SportsVerse Academy",
    val favoriteSport: String = "Cricket",
    val avatarStyle: String = "Jersey #7 Blue",
    val level: Int = 1,
    val xp: Int = 150,
    val maxXpForLevel: Int = 500,
    val coins: Int = 1200,
    val diamonds: Int = 45,
    val energy: Int = 85,
    val maxEnergy: Int = 100,
    val streakDays: Int = 5,
    val totalPracticeMinutes: Int = 145,
    val isPremium: Boolean = false,
    val lastRefillTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sport_progress")
data class SportProgressEntity(
    @PrimaryKey val sportId: String,
    val completedLessonIds: String = "", // Comma-separated list of completed lesson IDs
    val totalXpEarned: Int = 0,
    val highestQuizScore: Int = 0,
    val practiceMinutes: Int = 0,
    val isMastered: Boolean = false
)

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
    @PrimaryKey val missionId: String,
    val title: String,
    val description: String,
    val targetCount: Int,
    val currentCount: Int,
    val xpReward: Int,
    val coinReward: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val achievementId: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: String = "",
    val xpReward: Int,
    val coinReward: Int,
    val diamondReward: Int
)

@Entity(tableName = "practice_logs")
data class PracticeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sportId: String,
    val modeName: String,
    val durationSeconds: Int,
    val postureScore: Int,
    val timingAccuracy: Int,
    val dateTimestamp: Long = System.currentTimeMillis()
)
