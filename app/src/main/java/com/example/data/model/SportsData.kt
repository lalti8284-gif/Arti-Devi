package com.example.data.model

data class SportItem(
    val id: String,
    val name: String,
    val category: SportCategory,
    val iconEmoji: String,
    val description: String,
    val difficulty: String, // Beginner, Intermediate, Advanced, Pro
    val xpReward: Int,
    val popularityRank: Int,
    val isIndianOrigin: Boolean = false,
    val playersCount: String = "120K+"
)

enum class SportCategory {
    INDIAN_SPORTS,
    INTERNATIONAL,
    INDOOR,
    OUTDOOR,
    RACKET,
    MARTIAL_ARTS,
    COMBAT
}

data class SportLesson(
    val id: String,
    val sportId: String,
    val lessonNumber: Int,
    val title: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val coinReward: Int,
    val keyConcepts: List<String>,
    val videoUrl: String = "",
    val contentMarkdown: String
)

data class SportCoach(
    val id: String,
    val name: String,
    val title: String,
    val sportId: String,
    val rating: Float,
    val experienceYears: Int,
    val specialty: String,
    val quote: String,
    val studentsTrained: String
)

data class FamousPlayer(
    val id: String,
    val name: String,
    val country: String,
    val sportId: String,
    val role: String,
    val keyAchievements: List<String>,
    val bio: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class CareerRole(
    val id: String,
    val title: String,
    val category: String, // Player, Coach, Referee, Commentator, Analyst, Trainer
    val startingSalaryINR: String,
    val topSalaryINR: String,
    val eligibility: String,
    val keyCourses: List<String>,
    val govtJobsInfo: String,
    val description: String
)

data class LeaderboardUser(
    val rank: Int,
    val username: String,
    val level: Int,
    val xp: Int,
    val state: String,
    val avatarId: String,
    val isUser: Boolean = false
)
