package com.ultron.superai

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.Locale
import kotlin.concurrent.thread

class UltronBackgroundService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isRunning = false
    private val pcIpAddress = "192.168.1.108" // আপনার পিসির লোকাল আইপি
    private val port = 65000

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        Log.d("UltronAI", "Advanced Background Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "UltronBackgroundChannel"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ultron AI Autonomous Core",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setContentTitle("ULTRON SUPER AI - Autonomous Mode")
                .setContentText("Socket Bridge & Real-time Voice Guidance Active...")
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle("ULTRON SUPER AI - Autonomous Mode")
                .setContentText("Socket Bridge & Real-time Voice Guidance Active...")
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .build()
        }

        startForeground(1, notification)
        startAutonomousSocketLoop()

        return START_STICKY
    }

    private fun startAutonomousSocketLoop() {
        isRunning = true
        thread {
            while (isRunning) {
                try {
                    Thread.sleep(20000) // প্রতি ২০ সেকেন্ড পর পর পিসির ব্যাকএন্ডে রিকোয়েস্ট পাঠাবে
                    
                    val socket = Socket(pcIpAddress, port)
                    val writer = PrintWriter(socket.outputStream, true)
                    val reader = BufferedReader(InputStreamReader(socket.inputStream))
                    
                    writer.println("get_signal")
                    val response = reader.readLine()
                    
                    writer.close()
                    reader.close()
                    socket.close()

                    if (response != null) {
                        Log.d("UltronAI", "Received from PC: $response")
                        parseAndAnnounce(response)
                    }

                } catch (e: Exception) {
                    Log.e("UltronAI", "Socket communication error: {e.message}")
                }
            }
        }
    }

    private fun parseAndAnnounce(jsonResponse: String) {
        try {
            // সাধারণ টেক্সট বা জেসন পার্স করে ভয়েস রেডি করা
            if (jsonResponse.contains("pair")) {
                val announcement = "বস, নতুন মার্কেট সিগন্যাল পাওয়া গেছে। 100 ট্রেডিং প্রোটোকল ম্যাচ হয়েছে। দ্রুত চেক করুন।"
                speakOut(announcement)
            } else {
                speakOut("বস, উল্ট্রন সিস্টেম ফুললি সিকিউর এবং একটিভ আছে।")
            }
        } catch (e: Exception) {
            Log.e("UltronAI", "Parsing error: {e.message}")
        }
    }

    private fun speakOut(text: String) {
        if (::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLocale(Locale("bn", "BD"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setLocale(Locale.US)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        isRunning = false
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
