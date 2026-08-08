package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.SportCategory
import com.example.data.model.SportItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun SportsDirectoryScreen(
    viewModel: SportsViewModel
) {
    val sportsList by viewModel.sportsList.collectAsState()
    var search by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Indian Sports, 2: International

    val filteredSports = sportsList.filter { sport ->
        val matchesSearch = sport.name.contains(search, ignoreCase = true) || sport.description.contains(search, ignoreCase = true)
        val matchesTab = when (selectedTab) {
            1 -> sport.isIndianOrigin
            2 -> !sport.isIndianOrigin
            else -> true
        }
        matchesSearch && matchesTab
    }

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
                            text = "SPORTS ACADEMY CATALOG",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "18+ Official Curriculum Sports with Step-by-Step Drills",
                            color = TextMuted,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Search Bar
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            placeholder = { Text("Search Cricket, Kabaddi, Chess, Football...", color = TextMuted) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlue) },
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
                                .testTag("search_sports_input"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filter Tabs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterTabChip("ALL SPORTS (${sportsList.size})", selectedTab == 0) { selectedTab = 0 }
                            FilterTabChip("INDIAN ORIGIN 🇮🇳", selectedTab == 1) { selectedTab = 1 }
                            FilterTabChip("GLOBAL SPORTS 🌐", selectedTab == 2) { selectedTab = 2 }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredSports) { sport ->
                    SportGridCard(
                        sport = sport,
                        onClick = { viewModel.selectSport(sport) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ElectricBlue else DeepCardBg)
            .border(1.dp, if (isSelected) GoldYellow else GlassBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) TextWhite else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun SportGridCard(
    sport: SportItem,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.height(180.dp),
        glowColor = if (sport.isIndianOrigin) OrangeGlow else BlueGlow,
        borderColor = if (sport.isIndianOrigin) SportsOrange else ElectricBlue,
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
                Text(sport.iconEmoji, fontSize = 32.sp)
                if (sport.isIndianOrigin) {
                    Box(
                        modifier = Modifier
                            .background(SportsOrange.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .border(1.dp, SportsOrange, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("🇮🇳 INDIA", color = SportsOrange, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column {
                Text(
                    text = sport.name,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = sport.description,
                    color = TextMuted,
                    fontSize = 10.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(sport.difficulty, color = GoldYellow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("+${sport.xpReward} XP", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
