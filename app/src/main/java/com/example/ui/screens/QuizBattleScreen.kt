package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun QuizBattleScreen(
    viewModel: SportsViewModel
) {
    val questions by viewModel.quizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val answers by viewModel.userQuizAnswers.collectAsState()
    val isFinished by viewModel.isQuizFinished.collectAsState()
    val scorePercentage by viewModel.quizScorePercentage.collectAsState()

    val currentQuestion = questions.getOrNull(currentIndex)

    StadiumBackgroundCanvas {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = DarkMidnightBg, modifier = Modifier.fillMaxWidth().border(1.dp, GlassBorder)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.navigateTo(ScreenRoute.Home.route) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "QUIZ BATTLE ARENA",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isFinished) {
                    // Quiz Result Ceremony Card
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = GoldGlow,
                        borderColor = GoldYellow
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🏆 QUIZ COMPLETED!", color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("ACCURACY SCORE", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("$scorePercentage%", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 38.sp)

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("REWARDS GAINED", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("+150 XP ⚡", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("+300 COINS 🪙", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            GameButton(
                                text = "RETURN TO STADIUM",
                                icon = "🏟️",
                                gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                                glowColor = OrangeGlow,
                                onClick = { viewModel.navigateTo(ScreenRoute.Home.route) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else if (currentQuestion != null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            // Question Progress Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "QUESTION ${currentIndex + 1} OF ${questions.size}",
                                    color = ElectricBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "+100 XP",
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Question Card
                            GlassmorphicCard(
                                modifier = Modifier.fillMaxWidth(),
                                glowColor = BlueGlow
                            ) {
                                Text(
                                    text = currentQuestion.question,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Options List
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                currentQuestion.options.forEachIndexed { optIdx, optionText ->
                                    val isSelected = answers[currentIndex] == optIdx
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isSelected) SportsOrange else DeepCardBg)
                                            .border(1.5.dp, if (isSelected) GoldYellow else GlassBorder, RoundedCornerShape(14.dp))
                                            .clickable { viewModel.answerQuizQuestion(currentIndex, optIdx) }
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "${'A' + optIdx}. $optionText",
                                            color = TextWhite,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Next Button
                        GameButton(
                            text = if (currentIndex == questions.size - 1) "FINISH QUIZ" else "NEXT QUESTION →",
                            onClick = { viewModel.nextQuizQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("next_question_btn")
                        )
                    }
                }
            }
        }
    }
}
