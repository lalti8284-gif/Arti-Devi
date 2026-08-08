package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun LessonPlayerScreen(
    viewModel: SportsViewModel
) {
    val lesson by viewModel.selectedLesson.collectAsState()
    val activeLesson = lesson ?: return

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
                        IconButton(onClick = { viewModel.navigateTo(ScreenRoute.SportDetail.route) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LESSON ${activeLesson.lessonNumber}",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Video Player Container Frame
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    glowColor = BlueGlow
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_stadium_banner),
                            contentDescription = "Lesson Video Player",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                color = ElectricBlue,
                                shape = RoundedCornerShape(30.dp),
                                modifier = Modifier.size(60.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = TextWhite,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = activeLesson.title,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "${activeLesson.durationMinutes} Minutes • Reward: +${activeLesson.xpReward} XP & +${activeLesson.coinReward} Coins",
                    color = SportsOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = "KEY LEARNING CONCEPTS")

                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        activeLesson.keyConcepts.forEach { concept ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥 ", fontSize = 14.sp)
                                Text(concept, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = "LESSON INSTRUCTION GUIDE")

                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = activeLesson.contentMarkdown,
                        color = TextWhite,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameButton(
                        text = "ASK AI COACH",
                        icon = "🤖",
                        gradient = listOf(ElectricBlue, Color(0xFF0D47A1)),
                        glowColor = BlueGlow,
                        onClick = { viewModel.navigateTo(ScreenRoute.AICoach.route) },
                        modifier = Modifier.weight(1f)
                    )

                    GameButton(
                        text = "COMPLETE LESSON",
                        icon = "✅",
                        gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                        glowColor = OrangeGlow,
                        onClick = { viewModel.completeLessonCurrent() },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
