package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun CertificateScreen(
    viewModel: SportsViewModel
) {
    val profile by viewModel.userProfile.collectAsState()
    val sport by viewModel.selectedSport.collectAsState()

    val userName = profile?.username ?: "Champion_Player"
    val sportName = sport?.name ?: "Cricket"

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
                            text = "OFFICIAL ACADEMY CERTIFICATE",
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Formal Gold Bordered Certificate Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0F1A33), Color(0xFF080D1A))
                            )
                        )
                        .border(3.dp, GoldYellow, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🇮🇳 SPORTSVERSE INDIA ACADEMY", color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        Text("CERTIFICATE OF ATHLETIC MASTERY", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("This official credential certifies that", color = TextMuted, fontSize = 11.sp)
                        Text(userName.uppercase(), color = GoldYellow, fontWeight = FontWeight.Black, fontSize = 22.sp, textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("has successfully completed the pro curriculum and tactical drills for", color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
                        Text(sportName.uppercase(), color = ElectricBlue, fontWeight = FontWeight.Black, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("VERIFICATION ID:", color = TextMuted, fontSize = 9.sp)
                                Text("SV-2026-IND-${(100000..999999).random()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("DATE: 2026-08-05", color = TextMuted, fontSize = 9.sp)
                            }

                            // Simulated QR Code Frame
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(Color.White, RoundedCornerShape(6.dp))
                                    .border(1.dp, GoldYellow, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📱\nQR", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 10.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = "SHARE CREDENTIAL",
                        icon = "📲",
                        gradient = listOf(ElectricBlue, Color(0xFF0D47A1)),
                        glowColor = BlueGlow,
                        onClick = {
                            AudioEngine.playCoinSound()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    GameButton(
                        text = "DOWNLOAD PDF",
                        icon = "📄",
                        gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                        glowColor = OrangeGlow,
                        onClick = {
                            AudioEngine.playCoinSound()
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "download_cert_btn"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
