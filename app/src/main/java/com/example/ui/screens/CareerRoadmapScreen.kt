package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SportsRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenRoute
import com.example.ui.viewmodel.SportsViewModel

@Composable
fun CareerRoadmapScreen(
    viewModel: SportsViewModel
) {
    val repo = SportsRepository(com.example.data.local.SportsVerseDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current).userDao())
    val careers = repo.getCareerRoadmapList()

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
                            text = "SPORTS CAREER ROADMAP",
                            color = GoldYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
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
                    .padding(16.dp)
            ) {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = GoldGlow,
                    borderColor = GoldYellow
                ) {
                    Text(
                        text = "BUILD A HIGH-PAYING SPORTS CAREER IN INDIA",
                        color = GoldYellow,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Explore career paths from pro playing to international coaching, sports analytics, officiating & government recruitment.",
                        color = TextWhite,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                SectionHeader(title = "CAREER PATHWAYS")

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    careers.forEach { role ->
                        GlassmorphicCard(
                            modifier = Modifier.fillMaxWidth(),
                            glowColor = BlueGlow,
                            borderColor = ElectricBlue
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(role.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Surface(
                                        color = SportsOrange,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = role.category,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(role.description, color = TextMuted, fontSize = 12.sp)

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Start: ${role.startingSalaryINR}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text("Top Tier: ${role.topSalaryINR}", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 4.dp))

                                Text("• Eligibility: ${role.eligibility}", color = TextWhite, fontSize = 11.sp)
                                Text("• Required Certifications: ${role.keyCourses.joinToString(", ")}", color = TextWhite, fontSize = 11.sp)
                                Text("• Govt Jobs: ${role.govtJobsInfo}", color = SportsOrange, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
