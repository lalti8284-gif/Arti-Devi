package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun WelcomeScreen(
    viewModel: SportsViewModel
) {
    StadiumBackgroundCanvas {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Stadium Banner Card
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                glowColor = OrangeGlow,
                borderColor = SportsOrange
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_stadium_banner),
                        contentDescription = "Stadium Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Text(
                                text = "AAA SPORTS ACADEMY",
                                color = GoldYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Learn Like a Pro",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Welcome to SportsVerse",
                    color = TextWhite,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Master Cricket, Kabaddi, Football, Chess & 15+ Sports with AI Coaching & Real Camera Practice",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }

            // Authentication Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GameButton(
                    text = "START ATHLETE JOURNEY",
                    icon = "🔥",
                    gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                    glowColor = OrangeGlow,
                    onClick = {
                        viewModel.navigateTo(ScreenRoute.ProfileSetup.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "start_journey_btn"
                )

                GameButton(
                    text = "CONTINUE AS GUEST",
                    icon = "🎮",
                    gradient = listOf(ElectricBlue, Color(0xFF0D47A1)),
                    glowColor = BlueGlow,
                    onClick = {
                        viewModel.navigateTo(ScreenRoute.Home.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "guest_login_btn"
                )

                Text(
                    text = "By signing in, you agree to SportsVerse Terms of Service & Privacy Policy",
                    color = TextMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
