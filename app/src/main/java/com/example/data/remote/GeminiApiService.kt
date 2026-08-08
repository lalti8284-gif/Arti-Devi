package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getAICoachResponse(userPrompt: String, sportContext: String = "Cricket"): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateFallbackCoachResponse(userPrompt, sportContext)
        }

        try {
            val jsonPayload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", "You are Coach Arya, an elite AAA sports coach and technique specialist for SportsVerse India. The user is asking about $sportContext: '$userPrompt'. Provide an encouraging, highly practical response breaking down technique, stance, practice drill, and pro tips. Keep formatting crisp with bullet points.")
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext generateFallbackCoachResponse(userPrompt, sportContext)
                }
                val bodyStr = response.body?.string() ?: ""
                val jsonObj = JSONObject(bodyStr)
                val candidates = jsonObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", generateFallbackCoachResponse(userPrompt, sportContext))
                    }
                }
                return@withContext generateFallbackCoachResponse(userPrompt, sportContext)
            }
        } catch (e: Exception) {
            generateFallbackCoachResponse(userPrompt, sportContext)
        }
    }

    private fun generateFallbackCoachResponse(prompt: String, sport: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("batting") || lower.contains("stance") -> {
                "🏏 **Coach Arya's Pro Batting Guide ($sport):**\n\n" +
                "1. **Stance & Balance:** Stand with feet shoulder-width apart, knees slightly flexed. Keep head still and eyes aligned with the bowler's release point.\n" +
                "2. **Grip:** Use the 'V' grip with both hands pointing down between outer edge and spine of the bat.\n" +
                "3. **Backlift:** Raise bat toward second slip with elbow leading the movement.\n" +
                "4. **Drill:** Practice 50 shadow drives in front of a mirror daily to build muscle memory!"
            }
            lower.contains("bowling") || lower.contains("spin") || lower.contains("fast") -> {
                "⚡ **Coach Arya's Bowling Drill ($sport):**\n\n" +
                "1. **Run-Up:** Maintain smooth acceleration with consistent stride length.\n" +
                "2. **Gather & Jump:** Drive non-bowling arm up high while keeping hips pointing down the pitch.\n" +
                "3. **Wrist Position:** Snap your wrist at release for maximum seam control and spin/pace.\n" +
                "4. **Drill:** Set up single stump target bowling for 30 minutes!"
            }
            lower.contains("kabaddi") || lower.contains("raid") -> {
                "🦁 **Coach Arya's Kabaddi Raid Masterclass:**\n\n" +
                "1. **Cant Maintenance:** Keep unbroken chant of 'Kabaddi Kabaddi' with deep diaphragm breathing.\n" +
                "2. **Footwork:** Use side-steps, toe-touch fakes, and stay on ball of foot.\n" +
                "3. **Escape Route:** Always maintain awareness of the midline for quick retreat after touch!"
            }
            lower.contains("chess") -> {
                "♟️ **Coach Arya's Chess Strategy:**\n\n" +
                "1. **Control the Center:** Occupy e4/d4 squares early with pawns and knights.\n" +
                "2. **King Safety:** Castle within the first 10 moves.\n" +
                "3. **Tactical Awareness:** Look for forks, pins, and skewers before every turn!"
            }
            else -> {
                "🏆 **Coach Arya's Performance Tip for $sport:**\n\n" +
                "Consistent practice beats raw talent! Focus on core mechanics:\n" +
                "• Warm up with 10 mins dynamic stretching\n" +
                "• Master footwork drills (agility ladder / cone shuttles)\n" +
                "• Practice high-repetition shadow drills\n" +
                "• Record your posture using our Camera Practice Mode to analyze body angles!"
            }
        }
    }
}
