package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun AICoachScreen(
    viewModel: SportsViewModel
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val presetPrompts = listOf(
        "🏏 Teach me batting stance",
        "⚡ How to bowl a leg spin",
        "🤼 Kabaddi raiding footwork",
        "♟️ Chess openings strategy",
        "⚽ Football bending free-kick"
    )

    StadiumBackgroundCanvas {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = DarkMidnightBg, modifier = Modifier.fillMaxWidth().border(1.dp, GlassBorder)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.navigateTo(ScreenRoute.Home.route) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, GoldYellow, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_ai_coach),
                                contentDescription = "Coach Arya",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("COACH ARYA (AI)", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text("Gemini 3.5 Flash Technique Specialist", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            bottomBar = {
                Surface(color = DarkMidnightBg, modifier = Modifier.fillMaxWidth().border(1.dp, GlassBorder)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(12.dp)
                    ) {
                        // Quick Presets
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            items(presetPrompts) { prompt ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DeepCardBg)
                                        .border(1.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                        .clickable { viewModel.sendAiPrompt(prompt.substring(3)) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(prompt, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Text Input
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.sendAiPrompt("Teach me batting stance & posture")
                                }
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Assistant", tint = SportsOrange)
                            }

                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Ask Coach Arya any sports question...", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = ElectricBlue,
                                    unfocusedBorderColor = GlassBorder,
                                    focusedContainerColor = DeepCardBg,
                                    unfocusedContainerColor = DeepCardBg
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ai_coach_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank()) {
                                        viewModel.sendAiPrompt(inputText)
                                        inputText = ""
                                    }
                                },
                                modifier = Modifier
                                    .background(ElectricBlue, CircleShape)
                                    .testTag("send_prompt_btn")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = TextWhite)
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubbleItem(msg)
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = GoldYellow,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Coach Arya is formulating athletic advice...", color = GoldYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.sender == "User"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        GlassmorphicCard(
            modifier = Modifier.widthIn(max = 290.dp),
            glowColor = if (isUser) BlueGlow else OrangeGlow,
            borderColor = if (isUser) ElectricBlue else SportsOrange
        ) {
            Column {
                Text(
                    text = message.sender,
                    color = if (isUser) ElectricBlue else GoldYellow,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.text,
                    color = TextWhite,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
