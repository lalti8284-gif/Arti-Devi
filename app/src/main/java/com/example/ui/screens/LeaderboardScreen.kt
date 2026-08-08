package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SportsRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun LeaderboardScreen(
    viewModel: SportsViewModel
) {
    var selectedScope by remember { mutableStateOf(0) } // 0: India, 1: State, 2: School, 3: Friends
    val repo = SportsRepository(com.example.data.local.SportsVerseDatabase.getDatabase(LocalContext.current).userDao())
    val leaderboard = repo.getLeaderboardList()

    StadiumBackgroundCanvas {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = DarkMidnightBg, modifier = Modifier.fillMaxWidth().border(1.dp, GlassBorder)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "NATIONAL ATHLETE LEADERBOARD",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Season 1 Championship Rankings",
                            color = TextMuted,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Scope Tabs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterTabChip("INDIA 🇮🇳", selectedScope == 0) { selectedScope = 0 }
                            FilterTabChip("STATE", selectedScope == 1) { selectedScope = 1 }
                            FilterTabChip("SCHOOL", selectedScope == 2) { selectedScope = 2 }
                            FilterTabChip("FRIENDS", selectedScope == 3) { selectedScope = 3 }
                        }
                    }
                }
            },
            bottomBar = {
                GameBottomNavigation(
                    currentRoute = viewModel.currentRoute.collectAsState().value,
                    onNavigate = { route -> viewModel.navigateTo(route) }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(leaderboard) { user ->
                    val isUser = user.isUser
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = if (user.rank == 1) GoldGlow else if (isUser) OrangeGlow else BlueGlow,
                        borderColor = if (user.rank == 1) GoldYellow else if (isUser) SportsOrange else GlassBorder
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Rank Emblem
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (user.rank) {
                                                1 -> GoldYellow
                                                2 -> Color(0xFFC0C0C0)
                                                3 -> Color(0xFFCD7F32)
                                                else -> DeepCardBg
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (user.rank <= 3) "🥇" else "#${user.rank}",
                                        color = if (user.rank <= 3) Color.Black else TextWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = user.username,
                                            color = if (isUser) GoldYellow else TextWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        if (isUser) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(color = SportsOrange, shape = RoundedCornerShape(4.dp)) {
                                                Text("YOU", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 8.sp, modifier = Modifier.padding(horizontal = 4.dp))
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${user.state} • Level ${user.level}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Text(
                                text = "${user.xp} XP",
                                color = NeonGreen,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
