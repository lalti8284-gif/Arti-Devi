package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun RewardsScreen(
    viewModel: SportsViewModel
) {
    var isSpinning by remember { mutableStateOf(false) }
    var spinResultMsg by remember { mutableStateOf<String?>(null) }
    val rotationAnim = remember { Animatable(0f) }

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
                            text = "CHAMPION REWARDS CENTER",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Daily Lucky Spin, Mystery Chests & Season Gifts",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Lucky Spin Wheel Section
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = GoldGlow,
                    borderColor = GoldYellow
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎡 DAILY LUCKY WHEEL", color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        Text("Spin to win up to 2,000 Coins & 5 Diamonds", color = TextMuted, fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Animated Wheel Visual
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .rotate(rotationAnim.value)
                                .clip(RoundedCornerShape(70.dp))
                                .background(DeepCardBg)
                                .border(3.dp, GoldYellow, RoundedCornerShape(70.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎁\n2000 COINS", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (spinResultMsg != null) {
                            Text(spinResultMsg!!, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        GameButton(
                            text = if (isSpinning) "SPINNING..." else "FREE LUCKY SPIN",
                            icon = "🎰",
                            gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                            glowColor = OrangeGlow,
                            onClick = {
                                if (!isSpinning) {
                                    isSpinning = true
                                    spinResultMsg = null
                                    val reward = viewModel.spinLuckyWheel()
                                    spinResultMsg = "🎉 YOU WON $reward COINS & +5 DIAMONDS!"
                                    isSpinning = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(title = "MYSTERY TREASURE CHESTS")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TreasureBoxCard(
                        title = "BRONZE CHEST",
                        reward = "500 Coins",
                        icon = "📦",
                        borderColor = ElectricBlue,
                        onClick = { viewModel.spinLuckyWheel() },
                        modifier = Modifier.weight(1f)
                    )
                    TreasureBoxCard(
                        title = "GOLDEN CHEST",
                        reward = "20 Diamonds",
                        icon = "👑",
                        borderColor = GoldYellow,
                        onClick = { viewModel.spinLuckyWheel() },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TreasureBoxCard(
    title: String,
    reward: String,
    icon: String,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        modifier = modifier.height(130.dp),
        borderColor = borderColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(reward, color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 11.sp)
        }
    }
}
