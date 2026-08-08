package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DailyMissionEntity
import com.example.data.local.SportsVerseDatabase
import com.example.data.local.UserProfileEntity
import com.example.data.model.*
import com.example.data.repository.SportsRepository
import com.example.ui.components.AudioEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ScreenRoute(val route: String) {
    object Splash : ScreenRoute("splash")
    object Welcome : ScreenRoute("welcome")
    object ProfileSetup : ScreenRoute("profile_setup")
    object Home : ScreenRoute("home")
    object SportsDirectory : ScreenRoute("sports_directory")
    object SportDetail : ScreenRoute("sport_detail")
    object LessonPlayer : ScreenRoute("lesson_player")
    object AICoach : ScreenRoute("ai_coach")
    object CameraPractice : ScreenRoute("camera_practice")
    object CareerRoadmap : ScreenRoute("career_roadmap")
    object QuizBattle : ScreenRoute("quiz_battle")
    object Leaderboard : ScreenRoute("leaderboard")
    object RewardsCenter : ScreenRoute("rewards_center")
    object Certificate : ScreenRoute("certificate")
}

data class ChatMessage(
    val sender: String, // "User" or "Coach Arya"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class SportsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SportsVerseDatabase.getDatabase(application)
    private val repository = SportsRepository(db.userDao())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyMissions: StateFlow<List<DailyMissionEntity>> = repository.dailyMissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentRoute = MutableStateFlow<String>(ScreenRoute.Splash.route)

    val sportsList = MutableStateFlow<List<SportItem>>(emptyList())
    val selectedSport = MutableStateFlow<SportItem?>(null)
    val selectedLesson = MutableStateFlow<SportLesson?>(null)

    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<SportCategory?>(null)

    // AI Coach Chat
    val chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("Coach Arya", "Namaste Champion! I am Coach Arya, your personal AI Sports Mentor. What technique or sport shall we conquer today?")
        )
    )
    val isAiLoading = MutableStateFlow(false)

    // Practice Mode State
    val isCameraActive = MutableStateFlow(false)
    val practiceSeconds = MutableStateFlow(0)
    val postureScore = MutableStateFlow(92)
    val timingAccuracy = MutableStateFlow(88)

    // Quiz Mode State
    val quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val currentQuizIndex = MutableStateFlow(0)
    val userQuizAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val isQuizFinished = MutableStateFlow(false)
    val quizScorePercentage = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            repository.initDefaultDataIfEmpty()
            sportsList.value = repository.getSportsList()
            // Set default selected sport as Cricket
            selectedSport.value = sportsList.value.firstOrNull()
        }
    }

    fun navigateTo(route: String) {
        currentRoute.value = route
    }

    fun selectSport(sport: SportItem) {
        selectedSport.value = sport
        navigateTo(ScreenRoute.SportDetail.route)
    }

    fun startLesson(lesson: SportLesson) {
        selectedLesson.value = lesson
        navigateTo(ScreenRoute.LessonPlayer.route)
    }

    fun updateUserProfile(username: String, state: String, favoriteSport: String, avatarStyle: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            val updated = current.copy(
                username = username,
                state = state,
                favoriteSport = favoriteSport,
                avatarStyle = avatarStyle
            )
            db.userDao().insertOrUpdateProfile(updated)
            navigateTo(ScreenRoute.Home.route)
        }
    }

    fun sendAiPrompt(prompt: String) {
        if (prompt.isBlank()) return
        val currentList = chatMessages.value.toMutableList()
        currentList.add(ChatMessage("User", prompt))
        chatMessages.value = currentList
        isAiLoading.value = true

        viewModelScope.launch {
            val sportContext = selectedSport.value?.name ?: "Sports"
            val reply = repository.getAICoachReply(prompt, sportContext)
            val updatedList = chatMessages.value.toMutableList()
            updatedList.add(ChatMessage("Coach Arya", reply))
            chatMessages.value = updatedList
            isAiLoading.value = false
            AudioEngine.playCoinSound()
            repository.updateMissionProgress("m3", 1)
        }
    }

    fun startQuizForSelectedSport() {
        val sportId = selectedSport.value?.id ?: "cricket"
        val questions = repository.getQuizForSport(sportId)
        quizQuestions.value = questions
        currentQuizIndex.value = 0
        userQuizAnswers.value = mutableMapOf()
        isQuizFinished.value = false
        quizScorePercentage.value = 0
        navigateTo(ScreenRoute.QuizBattle.route)
    }

    fun answerQuizQuestion(questionIndex: Int, selectedOptionIndex: Int) {
        val currentAnswers = userQuizAnswers.value.toMutableMap()
        currentAnswers[questionIndex] = selectedOptionIndex
        userQuizAnswers.value = currentAnswers
    }

    fun nextQuizQuestion() {
        if (currentQuizIndex.value < quizQuestions.value.size - 1) {
            currentQuizIndex.value += 1
        } else {
            // Calculate Score
            var correctCount = 0
            val total = quizQuestions.value.size
            quizQuestions.value.forEachIndexed { idx, q ->
                if (userQuizAnswers.value[idx] == q.correctAnswerIndex) {
                    correctCount++
                }
            }
            val percentage = if (total > 0) (correctCount * 100) / total else 0
            quizScorePercentage.value = percentage
            isQuizFinished.value = true

            viewModelScope.launch {
                AudioEngine.playLevelUpSound()
                repository.addXpAndCoins(xpAmount = 150, coinsAmount = 300)
                repository.updateMissionProgress("m2", 1)
            }
        }
    }

    fun completeLessonCurrent() {
        viewModelScope.launch {
            val lesson = selectedLesson.value
            if (lesson != null) {
                AudioEngine.playLevelUpSound()
                repository.addXpAndCoins(xpAmount = lesson.xpReward, coinsAmount = lesson.coinReward)
            }
            navigateTo(ScreenRoute.SportDetail.route)
        }
    }

    fun recordPracticeSession(durationSec: Int) {
        viewModelScope.launch {
            val sportId = selectedSport.value?.id ?: "cricket"
            repository.savePracticeSession(
                sportId = sportId,
                mode = "Camera Pose Drills",
                durationSec = durationSec,
                postureScore = postureScore.value,
                accuracy = timingAccuracy.value
            )
            AudioEngine.playHitSound()
            repository.updateMissionProgress("m4", 1)
        }
    }

    fun claimMission(missionId: String) {
        viewModelScope.launch {
            AudioEngine.playCoinSound()
            repository.claimMissionReward(missionId)
        }
    }

    fun spinLuckyWheel(): Int {
        val rewardCoins = listOf(100, 250, 500, 1000, 2000).random()
        viewModelScope.launch {
            AudioEngine.playCoinSound()
            repository.addXpAndCoins(xpAmount = 100, coinsAmount = rewardCoins, diamondsAmount = 5)
        }
        return rewardCoins
    }
}
