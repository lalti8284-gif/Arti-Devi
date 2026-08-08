package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel
import kotlinx.coroutines.delay

@Composable
fun CameraPracticeScreen(
    viewModel: SportsViewModel
) {
    var isPracticing by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(0) }
    var postureScore by remember { mutableIntStateOf(92) }

    val infiniteTransition = rememberInfiniteTransition(label = "skeleton_motion")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(isPracticing) {
        if (isPracticing) {
            while (true) {
                delay(1000)
                timerSeconds += 1
                postureScore = (88..98).random()
            }
        }
    }

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
                            text = "CAMERA POSE & DRILL ANALYZER",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Camera Viewfinder Box with Skeleton Overlay
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    glowColor = OrangeGlow,
                    borderColor = SportsOrange
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Simulated Camera Feed background grid
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Grid lines
                            drawLine(Color.White.copy(alpha = 0.15f), Offset(w * 0.33f, 0f), Offset(w * 0.33f, h), 2f)
                            drawLine(Color.White.copy(alpha = 0.15f), Offset(w * 0.66f, 0f), Offset(w * 0.66f, h), 2f)
                            drawLine(Color.White.copy(alpha = 0.15f), Offset(0f, h * 0.5f), Offset(w, h * 0.5f), 2f)

                            // AI Pose Skeleton Overlay Lines (Head, Torso, Arms, Legs)
                            val head = Offset(w * 0.5f, h * 0.22f + pulse)
                            val neck = Offset(w * 0.5f, h * 0.32f)
                            val rShoulder = Offset(w * 0.35f, h * 0.36f)
                            val lShoulder = Offset(w * 0.65f, h * 0.36f)
                            val rElbow = Offset(w * 0.28f, h * 0.48f - pulse)
                            val lElbow = Offset(w * 0.72f, h * 0.48f + pulse)
                            val spine = Offset(w * 0.5f, h * 0.60f)
                            val rHip = Offset(w * 0.40f, h * 0.62f)
                            val lHip = Offset(w * 0.60f, h * 0.62f)
                            val rKnee = Offset(w * 0.38f, h * 0.78f)
                            val lKnee = Offset(w * 0.62f, h * 0.78f)

                            val skeletonColor = NeonGreen

                            // Head
                            drawCircle(skeletonColor, radius = 24f, center = head, style = Stroke(width = 4f))

                            // Spine & Shoulders
                            drawLine(skeletonColor, head, neck, strokeWidth = 5f)
                            drawLine(skeletonColor, rShoulder, lShoulder, strokeWidth = 5f)
                            drawLine(skeletonColor, neck, spine, strokeWidth = 5f)

                            // Arms
                            drawLine(skeletonColor, rShoulder, rElbow, strokeWidth = 5f)
                            drawLine(skeletonColor, lShoulder, lElbow, strokeWidth = 5f)

                            // Hips & Legs
                            drawLine(skeletonColor, spine, rHip, strokeWidth = 5f)
                            drawLine(skeletonColor, spine, lHip, strokeWidth = 5f)
                            drawLine(skeletonColor, rHip, rKnee, strokeWidth = 5f)
                            drawLine(skeletonColor, lHip, lKnee, strokeWidth = 5f)

                            // Key Joint Points
                            listOf(head, neck, rShoulder, lShoulder, rElbow, lElbow, rHip, lHip, rKnee, lKnee).forEach { j ->
                                drawCircle(GoldYellow, radius = 8f, center = j)
                            }
                        }

                        // Overlay Stats Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(if (isPracticing) NeonGreen else Color.Red, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPracticing) "LIVE DRILL" else "STANDBY",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(DeepCardBg, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "TIME: ${timerSeconds / 60}:${String.format("%02d", timerSeconds % 60)}",
                                    color = GoldYellow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Live Posture Feedback Banner
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(12.dp)
                                .background(DarkMidnightBg.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                                .border(1.dp, NeonGreen, RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("POSTURE SCORE: $postureScore%", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text("• Spine angle 92° (Optimal) • Weight transfer on front foot", color = TextWhite, fontSize = 11.sp)
                                }
                                Text("🎯 PERFECT", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Drill Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = if (isPracticing) "PAUSE DRILL" else "START CAMERA PRACTICE",
                        icon = "🎥",
                        gradient = if (isPracticing) listOf(Color(0xFFD32F2F), Color(0xFFB71C1C)) else listOf(SportsOrange, Color(0xFFFF5722)),
                        glowColor = OrangeGlow,
                        onClick = { isPracticing = !isPracticing },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_practice_btn")
                    )

                    if (timerSeconds > 0) {
                        GameButton(
                            text = "SAVE SESSION",
                            icon = "💾",
                            gradient = listOf(ElectricBlue, Color(0xFF0D47A1)),
                            glowColor = BlueGlow,
                            onClick = {
                                viewModel.recordPracticeSession(timerSeconds)
                                isPracticing = false
                                viewModel.navigateTo(ScreenRoute.Home.route)
                            },
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("save_practice_btn")
                        )
                    }
                }
            }
        }
    }
}
