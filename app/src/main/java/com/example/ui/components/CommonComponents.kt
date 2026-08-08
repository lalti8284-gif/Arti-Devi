package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun StadiumBackgroundCanvas(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stadium_lights")
    val lightSweep by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "light_sweep"
    )

    val particles = remember {
        List(25) {
            Offset(Random.nextFloat(), Random.nextFloat())
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkMidnightBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Radial stadium glow at center top
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.25f),
                        SportsOrange.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.25f),
                    radius = width * 0.85f
                ),
                radius = width * 0.85f,
                center = Offset(width * 0.5f, height * 0.25f)
            )

            // Dynamic light beam 1
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(ElectricBlue.copy(alpha = 0.4f), Color.Transparent),
                    start = Offset(width * lightSweep, 0f),
                    end = Offset(width * (lightSweep + 0.4f), height)
                ),
                start = Offset(width * lightSweep, 0f),
                end = Offset(width * (lightSweep + 0.4f), height),
                strokeWidth = 140f
            )

            // Dynamic light beam 2 (orange)
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(SportsOrange.copy(alpha = 0.25f), Color.Transparent),
                    start = Offset(width * (1f - lightSweep), 0f),
                    end = Offset(width * (0.6f - lightSweep), height)
                ),
                start = Offset(width * (1f - lightSweep), 0f),
                end = Offset(width * (0.6f - lightSweep), height),
                strokeWidth = 100f
            )

            // Particles
            particles.forEach { p ->
                drawCircle(
                    color = GoldYellow.copy(alpha = 0.35f),
                    radius = 3f,
                    center = Offset(p.x * width, p.y * height)
                )
            }
        }
        content()
    }
}

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    borderColor: Color = GlassBorder,
    glowColor: Color = BlueGlow,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    onClick: (() -> Unit)? = null,
    testTag: String = "glass_card",
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepCardBg.copy(alpha = 0.95f),
                        Color(0xFF0D1426).copy(alpha = 0.9f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.2f), glowColor)
                ),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = ElectricBlue),
                        onClick = {
                            AudioEngine.playClickSound()
                            onClick()
                        }
                    )
                } else Modifier
            )
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun GameTopBar(
    level: Int,
    xp: Int,
    maxXp: Int,
    coins: Int,
    diamonds: Int,
    energy: Int,
    maxEnergy: Int,
    username: String,
    onAvatarClick: () -> Unit,
    onRewardClick: () -> Unit,
    testTag: String = "top_bar"
) {
    Surface(
        color = DarkMidnightBg.copy(alpha = 0.92f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(ElectricBlue.copy(alpha = 0.3f), SportsOrange.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(0.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile & Level
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onAvatarClick() }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(ElectricBlue, SportsOrange))
                            )
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(DeepCardBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 22.sp)
                        }
                    }
                    // Level badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .background(GoldYellow, CircleShape)
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "L$level",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = username,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    // XP Bar
                    val xpProgress = (xp.toFloat() / maxXp.coerceAtLeast(1)).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(xpProgress)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(ElectricBlue, NeonGreen))
                                )
                        )
                    }
                    Text(
                        text = "$xp/$maxXp XP",
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                }
            }

            // Currency & Energy Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Coins
                CurrencyChip(
                    icon = "🪙",
                    value = coins.toString(),
                    color = GoldYellow,
                    onClick = onRewardClick
                )

                // Diamonds
                CurrencyChip(
                    icon = "💎",
                    value = diamonds.toString(),
                    color = ElectricBlue,
                    onClick = onRewardClick
                )

                // Energy
                CurrencyChip(
                    icon = "⚡",
                    value = "$energy/$maxEnergy",
                    color = SportsOrange,
                    onClick = onRewardClick
                )
            }
        }
    }
}

@Composable
fun CurrencyChip(
    icon: String,
    value: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DeepCardBg)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(icon, fontSize = 12.sp)
            Text(
                text = value,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: List<Color> = listOf(ElectricBlue, Color(0xFF0044CC)),
    glowColor: Color = BlueGlow,
    icon: String? = null,
    testTag: String = "game_button"
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = {
            AudioEngine.playClickSound()
            onClick()
        },
        modifier = modifier
            .testTag(testTag)
            .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = glowColor)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(16.dp),
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
                .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Text(icon, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .background(SportsOrange, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp)
                )
            }
        }

        if (actionText != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    color = ElectricBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
