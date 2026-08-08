package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AudioEngine
import com.example.ui.components.StadiumBackgroundCanvas
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SportsViewModel
) {
    var progress by remember { mutableFloatStateOf(0f) }

    val scaleAnim by animateFloatAsState(
        targetValue = if (progress > 0.3f) 1.05f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale_anim"
    )

    LaunchedEffect(Unit) {
        AudioEngine.playWhistleSound()
        for (i in 1..100) {
            progress = i / 100f
            delay(20)
        }
        delay(400)
        viewModel.navigateTo(ScreenRoute.Welcome.route)
    }

    StadiumBackgroundCanvas {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Emblem / Logo
            Box(
                modifier = Modifier
                    .scale(scaleAnim)
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_fg),
                    contentDescription = "SportsVerse Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SPORTSVERSE INDIA",
                color = TextWhite,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                letterSpacing = 2.sp
            )

            Text(
                text = "PLAY • LEARN • PRACTICE • BECOME A CHAMPION",
                color = SportsOrange,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // AAA Game Loading Bar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(260.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("PREPARING STADIUM...", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("${(progress * 100).toInt()}%", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(DeepCardBg, RoundedCornerShape(5.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                Brush.horizontalGradient(listOf(ElectricBlue, SportsOrange, GoldYellow)),
                                RoundedCornerShape(5.dp)
                            )
                    )
                }
            }
        }
    }
}
