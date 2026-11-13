package com.experai.zoro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.concurrent.atomic.AtomicBoolean

class ZoroForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "zoro_background_channel"
        const val ACTION_START = "com.experai.zoro.START_SERVICE"
        const val ACTION_STOP = "com.experai.zoro.STOP_SERVICE"
        private val isRunning = AtomicBoolean(false)
    }

    private lateinit var audioManager: AudioManager

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(AudioManager::class.java)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startListening(intent.getStringExtra("mode") ?: "standard")
            }
            ACTION_STOP -> {
                stopListening()
            }
        }
        return START_STICKY
    }

    private fun startListening(mode: String) {
        if (isRunning.getAndSet(true)) return

        val notification = buildNotification(mode)
        startForeground(1, notification)

        val focusRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .build()
        } else {
            null
        }

        if (focusRequest != null) {
            audioManager.requestAudioFocus(focusRequest)
        }
    }

    private fun stopListening() {
        isRunning.set(false)
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ZORO Background Assistant",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Shows when ZORO is active in the background"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(mode: String): Notification {
        val label = when (mode) {
            "always" -> "Always Available"
            "saver" -> "Energy Saver"
            "privacy" -> "Privacy Mode"
            "headset" -> "Headset Mode"
            "offline" -> "Offline Mode"
            else -> "Standard Mode"
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ZORO Assistant")
            .setContentText("Listening for \"Hey ZORO\" • $label")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        isRunning.set(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
