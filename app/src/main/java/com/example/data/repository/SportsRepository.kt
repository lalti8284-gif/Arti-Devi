package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import com.example.data.remote.GeminiApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class SportsRepository(private val userDao: UserDao) {

    private val geminiApi = GeminiApiService()

    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val dailyMissions: Flow<List<DailyMissionEntity>> = userDao.getAllMissions()
    val achievements: Flow<List<AchievementEntity>> = userDao.getAllAchievements()
    val practiceLogs: Flow<List<PracticeLogEntity>> = userDao.getPracticeLogs()

    suspend fun initDefaultDataIfEmpty() {
        var profile = userDao.getUserProfileOnce()
        if (profile == null) {
            profile = UserProfileEntity()
            userDao.insertOrUpdateProfile(profile)
        }

        val missions = userDao.getAllMissions().firstOrNull()
        if (missions.isNullOrEmpty()) {
            userDao.insertMissions(
                listOf(
                    DailyMissionEntity("m1", "Daily Training Session", "Practice any sport drill for 15 mins", 15, 5, 100, 200),
                    DailyMissionEntity("m2", "Quiz Master", "Complete 1 sports quiz with 80%+ score", 1, 0, 80, 150),
                    DailyMissionEntity("m3", "AI Coach Consultation", "Ask Coach Arya a technique question", 1, 0, 50, 100),
                    DailyMissionEntity("m4", "Posture Analysis", "Perform a camera reaction test", 1, 0, 120, 250)
                )
            )
        }

        val achs = userDao.getAllAchievements().firstOrNull()
        if (achs.isNullOrEmpty()) {
            userDao.insertAchievements(
                listOf(
                    AchievementEntity("a1", "Rookie Athlete", "Complete your first sports lesson", "trophy", true, "2026-08-01", 100, 200, 5),
                    AchievementEntity("a2", "Cricket Phenom", "Master all 5 Cricket modules", "bat", false, "", 300, 500, 20),
                    AchievementEntity("a3", "Streak Warrior", "Maintain a 7-day practice streak", "fire", true, "2026-08-04", 250, 300, 10),
                    AchievementEntity("a4", "Kabaddi Lion", "Score 90%+ in Kabaddi rules quiz", "shield", false, "", 200, 350, 15),
                    AchievementEntity("a5", "Grandmaster Mind", "Complete Chess tactical opening drills", "crown", false, "", 400, 600, 25)
                )
            )
        }
    }

    suspend fun addXpAndCoins(xpAmount: Int, coinsAmount: Int, diamondsAmount: Int = 0) {
        val current = userDao.getUserProfileOnce() ?: UserProfileEntity()
        var newXp = current.xp + xpAmount
        var newLevel = current.level
        var maxXp = current.maxXpForLevel

        if (newXp >= maxXp) {
            newLevel += 1
            newXp -= maxXp
            maxXp = (maxXp * 1.25).toInt()
        }

        val updated = current.copy(
            level = newLevel,
            xp = newXp,
            maxXpForLevel = maxXp,
            coins = current.coins + coinsAmount,
            diamonds = current.diamonds + diamondsAmount
        )
        userDao.insertOrUpdateProfile(updated)
    }

    suspend fun updateMissionProgress(missionId: String, increment: Int) {
        val missions = userDao.getAllMissions().firstOrNull() ?: emptyList()
        val m = missions.find { it.missionId == missionId } ?: return
        val newCurrent = (m.currentCount + increment).coerceAtMost(m.targetCount)
        val isDone = newCurrent >= m.targetCount
        userDao.updateMission(m.copy(currentCount = newCurrent, isCompleted = isDone))
    }

    suspend fun claimMissionReward(missionId: String) {
        val missions = userDao.getAllMissions().firstOrNull() ?: emptyList()
        val m = missions.find { it.missionId == missionId } ?: return
        if (m.isCompleted && !m.isClaimed) {
            userDao.updateMission(m.copy(isClaimed = true))
            addXpAndCoins(m.xpReward, m.coinReward)
        }
    }

    suspend fun savePracticeSession(sportId: String, mode: String, durationSec: Int, postureScore: Int, accuracy: Int) {
        userDao.insertPracticeLog(
            PracticeLogEntity(
                sportId = sportId,
                modeName = mode,
                durationSeconds = durationSec,
                postureScore = postureScore,
                timingAccuracy = accuracy
            )
        )
        addXpAndCoins(xpAmount = 120, coinsAmount = 250)
        updateMissionProgress("m1", durationSec / 60)
    }

    suspend fun getAICoachReply(prompt: String, sport: String): String {
        return geminiApi.getAICoachResponse(prompt, sport)
    }

    // Curated Sports Data Engine
    fun getSportsList(): List<SportItem> = listOf(
        // Indian Sports
        SportItem("cricket", "Cricket", SportCategory.INDIAN_SPORTS, "🏏", "Master Batting, Bowling, Fielding & Match Tactics", "Beginner to Pro", 500, 1, true, "450K+"),
        SportItem("kabaddi", "Kabaddi", SportCategory.INDIAN_SPORTS, "🤼", "Master Raiding, Ankle Holds, Tackle Angles & Canting", "Intermediate", 450, 2, true, "280K+"),
        SportItem("kho_kho", "Kho Kho", SportCategory.INDIAN_SPORTS, "🏃", "Learn Dodge, Pole Dive, Chasing & Chains", "Beginner", 350, 5, true, "150K+"),
        SportItem("wrestling", "Wrestling (Kushti)", SportCategory.INDIAN_SPORTS, "🤼‍♂️", "Traditional Akada pin-downs, Throws & Stance", "Advanced", 500, 8, true, "90K+"),
        SportItem("hockey", "Field Hockey", SportCategory.INDIAN_SPORTS, "🏑", "Dribble, Drag-flick, Scoop & Passing Drills", "Intermediate", 400, 3, true, "210K+"),
        SportItem("volleyball", "Volleyball", SportCategory.INDIAN_SPORTS, "🏐", "Spike, Block, Serve & Setting Mechanics", "Beginner", 350, 9, true, "180K+"),
        SportItem("athletics", "Athletics & Track", SportCategory.INDIAN_SPORTS, "👟", "Sprinting block start, Long Jump & Javelin throw", "Beginner", 300, 7, true, "310K+"),
        SportItem("chess", "Chess (Shatranj)", SportCategory.INDIAN_SPORTS, "♟️", "Openings, Endgame tactics & Grandmaster Mindset", "Intermediate", 600, 4, true, "390K+"),

        // International Sports
        SportItem("football", "Football (Soccer)", SportCategory.INTERNATIONAL, "⚽", "Passing, Bend Kicks, Juggling & Positioning", "Beginner to Pro", 500, 6, false, "500K+"),
        SportItem("basketball", "Basketball", SportCategory.INTERNATIONAL, "🏀", "Dribble, Jump Shot, Crossover & Layups", "Intermediate", 450, 10, false, "220K+"),
        SportItem("tennis", "Lawn Tennis", SportCategory.INTERNATIONAL, "🎾", "Forehand Topspin, Slice Serve & Net Volleys", "Advanced", 550, 11, false, "160K+"),
        SportItem("badminton", "Badminton", SportCategory.INTERNATIONAL, "🏸", "Smash Angle, Net Play, Clear & Footwork", "Beginner", 400, 12, false, "340K+"),
        SportItem("boxing", "Boxing & Combat", SportCategory.INTERNATIONAL, "🥊", "Jab, Cross, Hook, Slip & Foot Dynamics", "Advanced", 500, 13, false, "120K+"),
        SportItem("swimming", "Swimming", SportCategory.INTERNATIONAL, "🏊", "Freestyle stroke, Breathing tempo & Flip turns", "Intermediate", 400, 14, false, "190K+"),
        SportItem("table_tennis", "Table Tennis", SportCategory.INTERNATIONAL, "🏓", "Loop Drive, Backspin Serve & Counter Strike", "Intermediate", 350, 15, false, "200K+"),
        SportItem("archery", "Archery", SportCategory.INTERNATIONAL, "🏹", "Draw stance, Release anchor & Wind adjustment", "Advanced", 450, 16, false, "85K+"),
        SportItem("cycling", "Cycling", SportCategory.INTERNATIONAL, "🚴", "Cadence control, Aerodynamics & Sprint finish", "Beginner", 300, 17, false, "110K+"),
        SportItem("golf", "Golf", SportCategory.INTERNATIONAL, "⛳", "Swing arc, Putting green alignment & Wedge play", "Pro", 600, 18, false, "60K+")
    )

    fun getLessonsForSport(sportId: String): List<SportLesson> {
        return listOf(
            SportLesson(
                id = "${sportId}_l1",
                sportId = sportId,
                lessonNumber = 1,
                title = "Foundations & Core Rules",
                durationMinutes = 10,
                xpReward = 150,
                coinReward = 200,
                keyConcepts = listOf("Ground Layout", "Equipment Specs", "Scoring System", "Basic Stance"),
                contentMarkdown = "Learn the fundamental pitch dimensions, official gear requirements, safety rules, and foundational posture."
            ),
            SportLesson(
                id = "${sportId}_l2",
                sportId = sportId,
                lessonNumber = 2,
                title = "Mastering Primary Technique",
                durationMinutes = 15,
                xpReward = 200,
                coinReward = 300,
                keyConcepts = listOf("Hand-Eye Coordination", "Weight Transfer", "Follow-Through", "Balance Point"),
                contentMarkdown = "Break down the core stroke, throw, or movement with step-by-step kinetic chain execution."
            ),
            SportLesson(
                id = "${sportId}_l3",
                sportId = sportId,
                lessonNumber = 3,
                title = "Advanced Tactical Play",
                durationMinutes = 20,
                xpReward = 250,
                coinReward = 400,
                keyConcepts = listOf("Field Placement", "Opponent Reading", "Pacing Strategy", "Pressure Execution"),
                contentMarkdown = "Learn real match situational tactics, field placement, anti-opponent counter strategies."
            ),
            SportLesson(
                id = "${sportId}_l4",
                sportId = sportId,
                lessonNumber = 4,
                title = "Pro Fitness & Conditioning",
                durationMinutes = 12,
                xpReward = 180,
                coinReward = 250,
                keyConcepts = listOf("Agility Ladder", "Reaction Drills", "Core Stability", "Recovery Routine"),
                contentMarkdown = "High-intensity drills tailored specifically for endurance, explosive speed, and injury prevention."
            )
        )
    }

    fun getQuizForSport(sportId: String): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = "q1",
                question = "What is the key element of proper balance in $sportId stance?",
                options = listOf("Feet shoulder-width apart with flexed knees", "Leaning far forward on toes", "Standing stiff on heels", "Crossing feet over"),
                correctAnswerIndex = 0,
                explanation = "A balanced stance requires feet shoulder-width apart with flexed knees for optimal center of gravity."
            ),
            QuizQuestion(
                id = "q2",
                question = "Which kinetic principle gives maximum power in executing a shot/throw?",
                options = listOf("Only arm movement", "Sequential weight transfer from lower body to upper body", "Closing eyes at impact", "Static torso"),
                correctAnswerIndex = 1,
                explanation = "Power is generated through the kinetic chain starting from ground force, hip rotator, and follow-through."
            ),
            QuizQuestion(
                id = "q3",
                question = "How often should an athlete perform reaction agility drills?",
                options = listOf("Once a month", "At least 3-4 times weekly", "Never", "Only before finals"),
                correctAnswerIndex = 1,
                explanation = "Neuro-muscular speed and visual reaction require high-frequency practice (3-4 times a week)."
            )
        )
    }

    fun getCareerRoadmapList(): List<CareerRole> = listOf(
        CareerRole(
            id = "c1",
            title = "Professional Athlete / Player",
            category = "Player",
            startingSalaryINR = "₹6 Lakhs / yr",
            topSalaryINR = "₹15 Crores+ / yr",
            eligibility = "State / National Trials, Academy Drafts",
            keyCourses = listOf("Sports Excellence Program", "High Performance Youth Camp"),
            govtJobsInfo = "Direct Group A/B recruitment in Railways, Defense, Bank Sports Quota",
            description = "Compete in national leagues (IPL, PKL, ISL), represent India in international tournaments, Olympics and World Cups."
        ),
        CareerRole(
            id = "c2",
            title = "High-Performance Sports Coach",
            category = "Coach",
            startingSalaryINR = "₹4.5 Lakhs / yr",
            topSalaryINR = "₹60 Lakhs / yr",
            eligibility = "NIS Diploma in Sports Coaching (SAI) / Level 1-3 Certification",
            keyCourses = listOf("SAI NIS Diploma", "B.P.Ed / M.P.Ed", "ICC / FIFA License"),
            govtJobsInfo = "Sports Authority of India (SAI) Coach recruitment",
            description = "Train future champions, formulate tactical strategies, analyze match footage and lead professional clubs."
        ),
        CareerRole(
            id = "c3",
            title = "International Match Referee / Umpire",
            category = "Referee",
            startingSalaryINR = "₹3.5 Lakhs / yr",
            topSalaryINR = "₹40 Lakhs / yr",
            eligibility = "State Federation Exam & Physical Fitness Fitness Test",
            keyCourses = listOf("BCCI/FIH Officiating Certification", "VAR / DRS Specialist Training"),
            govtJobsInfo = "Federation Panel Referee Roster & SAI Officials Board",
            description = "Enforce international rules, manage video referee reviews, and oversee high-stakes championship games."
        ),
        CareerRole(
            id = "c4",
            title = "Sports Physiotherapist & Fitness Trainer",
            category = "Fitness Trainer",
            startingSalaryINR = "₹5 Lakhs / yr",
            topSalaryINR = "₹50 Lakhs / yr",
            eligibility = "BPT / MPT (Sports Rehabilitation) or CSCS Certification",
            keyCourses = listOf("B.P.T Sports Specialization", "NSCA Certified Strength & Conditioning"),
            govtJobsInfo = "Team India High Performance Centers & Army Sports Institute",
            description = "Design bio-mechanical recovery programs, manage athlete nutrition, prevent injuries and boost peak athletic power."
        ),
        CareerRole(
            id = "c5",
            title = "Sports Commentator & Broadcaster",
            category = "Commentator",
            startingSalaryINR = "₹4 Lakhs / yr",
            topSalaryINR = "₹1 Crore+ / yr",
            eligibility = "Journalism / Mass Media degree or Ex-Player experience",
            keyCourses = listOf("Sports Journalism Diploma", "Broadcast Presentation & Voice Control"),
            govtJobsInfo = "Doordarshan Sports & All India Radio Panel",
            description = "Bring live match excitement to millions of viewers with real-time play-by-play commentary and tactical insights."
        ),
        CareerRole(
            id = "c6",
            title = "Sports Performance Data Analyst",
            category = "Analyst",
            startingSalaryINR = "₹6 Lakhs / yr",
            topSalaryINR = "₹45 Lakhs / yr",
            eligibility = "B.Sc/B.Tech in Data Analytics + Sports Science Certification",
            keyCourses = listOf("Python for Sports Analytics", "Hawk-Eye & Wearable Metrics"),
            govtJobsInfo = "SAI Sports Science Wings & Olympic Gold Quest",
            description = "Use AI, computer vision, and wearable sensors to uncover tactical opponent weaknesses and optimize player performance."
        )
    )

    fun getLeaderboardList(): List<LeaderboardUser> = listOf(
        LeaderboardUser(1, "Viraj_Strike7", 48, 18450, "Maharashtra", "Jersey #7 Blue"),
        LeaderboardUser(2, "Ananya_Smash", 45, 16900, "Karnataka", "Neon Flame Kit"),
        LeaderboardUser(3, "Rohan_Rider", 42, 15300, "Punjab", "Gold Elite Armor"),
        LeaderboardUser(4, "Champion_Player", 1, 150, "Maharashtra", "Jersey #7 Blue", isUser = true),
        LeaderboardUser(5, "Kabaddi_Lion_99", 39, 13800, "Haryana", "Red Tiger Uniform"),
        LeaderboardUser(6, "Grandmaster_Aarav", 36, 12400, "Tamil Nadu", "Cyber Black Suit"),
        LeaderboardUser(7, "Sneha_Goalie", 34, 11100, "West Bengal", "Electric Green Kit"),
        LeaderboardUser(8, "Pritam_Track", 31, 9800, "Kerala", "Solar Orange Stride")
    )
}
