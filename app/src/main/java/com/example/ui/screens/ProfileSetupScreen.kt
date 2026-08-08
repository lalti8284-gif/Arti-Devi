package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun ProfileSetupScreen(
    viewModel: SportsViewModel
) {
    var username by remember { mutableStateOf("Champion_Rider") }
    var state by remember { mutableStateOf("Maharashtra") }
    var favoriteSport by remember { mutableStateOf("Cricket") }
    var selectedAvatar by remember { mutableStateOf("Jersey #7 Blue") }

    val avatars = listOf(
        "Jersey #7 Blue" to "🏏",
        "Neon Flame Kit" to "⚡",
        "Red Tiger Uniform" to "🐯",
        "Gold Elite Armor" to "👑",
        "Cyber Black Suit" to "🕶️",
        "Electric Green Kit" to "⚽"
    )

    val sports = listOf("Cricket", "Kabaddi", "Kho Kho", "Hockey", "Football", "Chess", "Tennis", "Badminton")
    val states = listOf("Maharashtra", "Punjab", "Haryana", "Karnataka", "Tamil Nadu", "Delhi", "West Bengal", "Gujarat")

    StadiumBackgroundCanvas {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "CREATE ATHLETE PROFILE",
                color = GoldYellow,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            )
            Text(
                text = "Customize your avatar and primary sports specialization",
                color = TextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Choose Avatar
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = BlueGlow
            ) {
                Text(
                    text = "SELECT CHAMPION AVATAR",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    avatars.take(3).forEach { (name, emoji) ->
                        val isSelected = selectedAvatar == name
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedAvatar = name }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ElectricBlue else DeepCardBg)
                                    .border(2.dp, if (isSelected) GoldYellow else GlassBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name.split(" ").first(),
                                color = if (isSelected) GoldYellow else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Player Name Input
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text("ATHLETE USERNAME", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = DeepCardBg,
                        unfocusedContainerColor = DeepCardBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Favorite Sport
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text("FAVORITE PRIMARY SPORT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sports) { sport ->
                        val isSelected = favoriteSport == sport
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) SportsOrange else DeepCardBg)
                                .border(1.dp, if (isSelected) GoldYellow else GlassBorder, RoundedCornerShape(10.dp))
                                .clickable { favoriteSport = sport }
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = sport,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // State Selection
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text("STATE / REGION (FOR LEADERBOARDS)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(states) { st ->
                        val isSelected = state == st
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricBlue else DeepCardBg)
                                .border(1.dp, if (isSelected) GoldYellow else GlassBorder, RoundedCornerShape(10.dp))
                                .clickable { state = st }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st,
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GameButton(
                text = "CONFIRM & ENTER STADIUM",
                icon = "🏆",
                gradient = listOf(SportsOrange, Color(0xFFFF5722)),
                glowColor = OrangeGlow,
                onClick = {
                    viewModel.updateUserProfile(username, state, favoriteSport, selectedAvatar)
                },
                modifier = Modifier.fillMaxWidth(),
                testTag = "save_profile_btn"
            )
        }
    }
}
