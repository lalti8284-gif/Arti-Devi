package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
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
fun HomeScreen(
    viewModel: SportsViewModel
) {
    val profile by viewModel.userProfile.collectAsState()
    val missions by viewModel.dailyMissions.collectAsState()

    val level = profile?.level ?: 1
    val xp = profile?.xp ?: 150
    val maxXp = profile?.maxXpForLevel ?: 500
    val coins = profile?.coins ?: 1200
    val diamonds = profile?.diamonds ?: 45
    val energy = profile?.energy ?: 85
    val maxEnergy = profile?.maxEnergy ?: 100
    val username = profile?.username ?: "Champion_Rider"
    val favoriteSport = profile?.favoriteSport ?: "Cricket"

    StadiumBackgroundCanvas {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                GameTopBar(
                    level = level,
                    xp = xp,
                    maxXp = maxXp,
                    coins = coins,
                    diamonds = diamonds,
                    energy = energy,
                    maxEnergy = maxEnergy,
                    username = username,
                    onAvatarClick = { viewModel.navigateTo(ScreenRoute.ProfileSetup.route) },
                    onRewardClick = { viewModel.navigateTo(ScreenRoute.RewardsCenter.route) }
                )
            },
            bottomBar = {
                GameBottomNavigation(
                    currentRoute = viewModel.currentRoute.collectAsState().value,
                    onNavigate = { route -> viewModel.navigateTo(route) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Featured Hero Banner Card
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = OrangeGlow,
                    borderColor = SportsOrange
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_stadium_banner),
                            contentDescription = "Stadium",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(DarkMidnightBg.copy(alpha = 0.85f), Color.Transparent)
                                    )
                                )
                                .padding(14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(modifier = Modifier.width(220.dp)) {
                                Text(
                                    text = "CURRENT SPECIALIZATION",
                                    color = GoldYellow,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "$favoriteSport Mastery",
                                    color = TextWhite,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                GameButton(
                                    text = "CONTINUE LESSON",
                                    icon = "⚡",
                                    gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                                    glowColor = OrangeGlow,
                                    onClick = {
                                        viewModel.navigateTo(ScreenRoute.SportsDirectory.route)
                                    },
                                    modifier = Modifier
                                        .height(38.dp)
                                        .fillMaxWidth(0.9f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = "CORE GAME MODES", subtitle = "Select your training mode")

                // AAA Grid Cards
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickModeCard(
                            title = "LEARN SPORTS",
                            subtitle = "18+ Sports Curriculum",
                            icon = "🏏",
                            badgeText = "POPULAR",
                            glowColor = BlueGlow,
                            borderColor = ElectricBlue,
                            onClick = { viewModel.navigateTo(ScreenRoute.SportsDirectory.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickModeCard(
                            title = "AI COACH",
                            subtitle = "Coach Arya Q&A",
                            icon = "🤖",
                            badgeText = "24/7 AI",
                            glowColor = GoldGlow,
                            borderColor = GoldYellow,
                            onClick = { viewModel.navigateTo(ScreenRoute.AICoach.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickModeCard(
                            title = "CAMERA DRILLS",
                            subtitle = "Posture & Timing",
                            icon = "📷",
                            badgeText = "REAL CAMERA",
                            glowColor = OrangeGlow,
                            borderColor = SportsOrange,
                            onClick = { viewModel.navigateTo(ScreenRoute.CameraPractice.route) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickModeCard(
                            title = "CAREER ROADMAP",
                            subtitle = "Pro Player & Jobs",
                            icon = "💼",
                            badgeText = "GOVT JOBS",
                            glowColor = BlueGlow,
                            borderColor = CyberPurple,
                            onClick = { viewModel.navigateTo(ScreenRoute.CareerRoadmap.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickModeCard(
                            title = "QUIZ BATTLE",
                            subtitle = "Test & Earn Coins",
                            icon = "🎯",
                            badgeText = "+150 XP",
                            glowColor = OrangeGlow,
                            borderColor = SportsOrange,
                            onClick = { viewModel.startQuizForSelectedSport() },
                            modifier = Modifier.weight(1f)
                        )
                        QuickModeCard(
                            title = "REWARDS & SPIN",
                            subtitle = "Daily Chest & Gems",
                            icon = "🎁",
                            badgeText = "FREE SPIN",
                            glowColor = GoldGlow,
                            borderColor = GoldYellow,
                            onClick = { viewModel.navigateTo(ScreenRoute.RewardsCenter.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(
                    title = "DAILY MISSIONS",
                    subtitle = "Complete to earn XP & Coins",
                    actionText = "VIEW ALL",
                    onActionClick = {}
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    missions.forEach { mission ->
                        DailyMissionRow(
                            title = mission.title,
                            desc = mission.description,
                            current = mission.currentCount,
                            target = mission.targetCount,
                            xpReward = mission.xpReward,
                            coinReward = mission.coinReward,
                            isCompleted = mission.isCompleted,
                            isClaimed = mission.isClaimed,
                            onClaim = { viewModel.claimMission(mission.missionId) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun QuickModeCard(
    title: String,
    subtitle: String,
    icon: String,
    badgeText: String,
    glowColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        modifier = modifier.height(125.dp),
        glowColor = glowColor,
        borderColor = borderColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(icon, fontSize = 26.sp)
                Box(
                    modifier = Modifier
                        .background(borderColor.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                        .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = borderColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun DailyMissionRow(
    title: String,
    desc: String,
    current: Int,
    target: Int,
    xpReward: Int,
    coinReward: Int,
    isCompleted: Boolean,
    isClaimed: Boolean,
    onClaim: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = if (isCompleted) GoldGlow else BlueGlow
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(desc, color = TextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Progress: $current/$target", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("+$xpReward XP", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("+$coinReward Coins", color = GoldYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (isClaimed) {
                Text("CLAIMED", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            } else if (isCompleted) {
                Button(
                    onClick = onClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldYellow),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("CLAIM", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(DeepCardBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("IN PROGRESS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GameBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        color = DarkMidnightBg,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(1.dp, GlassBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentRoute == ScreenRoute.Home.route,
                onClick = { onNavigate(ScreenRoute.Home.route) },
                testTag = "nav_home"
            )
            BottomNavItem(
                icon = Icons.Default.Sports,
                label = "Learn",
                isSelected = currentRoute == ScreenRoute.SportsDirectory.route,
                onClick = { onNavigate(ScreenRoute.SportsDirectory.route) },
                testTag = "nav_learn"
            )
            BottomNavItem(
                icon = Icons.Default.SmartToy,
                label = "AI Coach",
                isSelected = currentRoute == ScreenRoute.AICoach.route,
                onClick = { onNavigate(ScreenRoute.AICoach.route) },
                testTag = "nav_ai_coach"
            )
            BottomNavItem(
                icon = Icons.Default.EmojiEvents,
                label = "Ranks",
                isSelected = currentRoute == ScreenRoute.Leaderboard.route,
                onClick = { onNavigate(ScreenRoute.Leaderboard.route) },
                testTag = "nav_ranks"
            )
            BottomNavItem(
                icon = Icons.Default.CardGiftcard,
                label = "Rewards",
                isSelected = currentRoute == ScreenRoute.RewardsCenter.route,
                onClick = { onNavigate(ScreenRoute.RewardsCenter.route) },
                testTag = "nav_rewards"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(testTag)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) SportsOrange else TextMuted,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (isSelected) TextWhite else TextMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 11.sp
        )
    }
}
