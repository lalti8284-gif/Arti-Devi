package com.example.ui.components

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object AudioEngine {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun playClickSound() {
        scope.launch {
            playTone(freq = 600f, durationMs = 35, volume = 0.2f)
        }
    }

    fun playCoinSound() {
        scope.launch {
            playTone(freq = 1200f, durationMs = 60, volume = 0.3f)
            playTone(freq = 1600f, durationMs = 90, volume = 0.3f)
        }
    }

    fun playLevelUpSound() {
        scope.launch {
            playTone(freq = 400f, durationMs = 80, volume = 0.3f)
            playTone(freq = 600f, durationMs = 80, volume = 0.3f)
            playTone(freq = 900f, durationMs = 80, volume = 0.3f)
            playTone(freq = 1200f, durationMs = 180, volume = 0.4f)
        }
    }

    fun playWhistleSound() {
        scope.launch {
            playTone(freq = 2400f, durationMs = 250, volume = 0.35f)
        }
    }

    fun playHitSound() {
        scope.launch {
            playTone(freq = 250f, durationMs = 80, volume = 0.4f)
        }
    }

    private fun playTone(freq: Float, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val sample = ByteArray(numSamples)
            val angle = 2.0 * Math.PI * freq / sampleRate

            for (i in 0 until numSamples) {
                val envelope = 1.0 - (i.toDouble() / numSamples) // Fade out
                val valSample = (sin(i * angle) * 32767 * volume * envelope).toInt().toShort()
                sample[i] = (valSample.toInt() and 0x00FF).toByte()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(sample.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(sample, 0, sample.size)
            audioTrack.play()
        } catch (e: Exception) {
            // Silently ignore audio playback errors
        }
    }
}
