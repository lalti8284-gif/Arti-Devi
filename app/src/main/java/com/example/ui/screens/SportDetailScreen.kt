package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SportsRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun SportDetailScreen(
    viewModel: SportsViewModel
) {
    val sport by viewModel.selectedSport.collectAsState()
    val activeSport = sport ?: viewModel.sportsList.collectAsState().value.firstOrNull()

    if (activeSport == null) return

    val repo = SportsRepository(com.example.data.local.SportsVerseDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).userDao())
    val lessons = repo.getLessonsForSport(activeSport.id)

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
                        IconButton(onClick = { viewModel.navigateTo(ScreenRoute.SportsDirectory.route) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeSport.name.uppercase(),
                            color = TextWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(activeSport.iconEmoji, fontSize = 28.sp)
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
                // Header Banner
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = OrangeGlow,
                    borderColor = SportsOrange
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "OFFICIAL ACADEMY CURRICULUM",
                                color = GoldYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${activeSport.playersCount} Athletes",
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeSport.description,
                            color = TextWhite,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GameButton(
                                text = "TAKE QUIZ",
                                icon = "🎯",
                                gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                                glowColor = OrangeGlow,
                                onClick = { viewModel.startQuizForSelectedSport() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            )
                            GameButton(
                                text = "CERTIFICATE",
                                icon = "📜",
                                gradient = listOf(ElectricBlue, Color(0xFF0D47A1)),
                                glowColor = BlueGlow,
                                onClick = { viewModel.navigateTo(ScreenRoute.Certificate.route) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(title = "LESSONS & MODULES", subtitle = "Complete lessons sequentially to gain XP")

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    lessons.forEach { lesson ->
                        GlassmorphicCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.startLesson(lesson) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(ElectricBlue),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${lesson.lessonNumber}",
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = lesson.title,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "${lesson.durationMinutes} mins • +${lesson.xpReward} XP",
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Text("START →", color = SportsOrange, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(title = "CORE GROUND SPECIFICATIONS & RULES")

                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("• Playing Field: Standard international regulation dimensions", color = TextWhite, fontSize = 12.sp)
                        Text("• Required Gear: Official certified protective equipment & athletic footwear", color = TextWhite, fontSize = 12.sp)
                        Text("• Scoring Mechanics: Points awarded via kinetic technique precision & rule compliance", color = TextWhite, fontSize = 12.sp)
                        Text("• Safety Protocol: Mandatory 10-minute dynamic warm-up prior to high-intensity drills", color = TextWhite, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
