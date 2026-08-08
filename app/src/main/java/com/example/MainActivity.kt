package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.SportsVerseTheme
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SportsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SportsVerseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SportsVerseApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SportsVerseApp(viewModel: SportsViewModel) {
    val currentRoute by viewModel.currentRoute.collectAsState()

    // Handle System Back Button
    BackHandler(enabled = currentRoute != ScreenRoute.Home.route && currentRoute != ScreenRoute.Splash.route) {
        viewModel.navigateTo(ScreenRoute.Home.route)
    }

    when (currentRoute) {
        ScreenRoute.Splash.route -> SplashScreen(viewModel)
        ScreenRoute.Welcome.route -> WelcomeScreen(viewModel)
        ScreenRoute.ProfileSetup.route -> ProfileSetupScreen(viewModel)
        ScreenRoute.Home.route -> HomeScreen(viewModel)
        ScreenRoute.SportsDirectory.route -> SportsDirectoryScreen(viewModel)
        ScreenRoute.SportDetail.route -> SportDetailScreen(viewModel)
        ScreenRoute.LessonPlayer.route -> LessonPlayerScreen(viewModel)
        ScreenRoute.AICoach.route -> AICoachScreen(viewModel)
        ScreenRoute.CameraPractice.route -> CameraPracticeScreen(viewModel)
        ScreenRoute.CareerRoadmap.route -> CareerRoadmapScreen(viewModel)
        ScreenRoute.QuizBattle.route -> QuizBattleScreen(viewModel)
        ScreenRoute.Leaderboard.route -> LeaderboardScreen(viewModel)
        ScreenRoute.RewardsCenter.route -> RewardsScreen(viewModel)
        ScreenRoute.Certificate.route -> CertificateScreen(viewModel)
        else -> HomeScreen(viewModel)
    }
}
