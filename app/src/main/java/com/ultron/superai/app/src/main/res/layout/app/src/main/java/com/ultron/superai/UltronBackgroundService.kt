package com.ultron.superai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class UltronBackgroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "ULTRON_CHANNEL")
            .setContentTitle("ULTRON SUPER AI")
            .setContentText("64 Systems & 100 Trading Brains Running 24/7 in Background")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(1001, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ULTRON_CHANNEL",
                "Ultron Background Engine",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
