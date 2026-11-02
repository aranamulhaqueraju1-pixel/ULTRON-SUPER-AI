package com.ultron.core

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var ultronSpeech: TextView
    private lateinit var securityBrain: UltronSecurityBrain
    private lateinit var tradingEngine: UltronTradingEngine
    private val SPEECH_REQUEST_CODE = 100
    private var isAutoTradeActive = false // Stealth & Auto Trade switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ultronSpeech = findViewById(R.id.ultronSpeech)
        val micButton = findViewById<ImageView>(R.id.micButton)

        // Initialize Core Brains
        securityBrain = UltronSecurityBrain(this)
        tradingEngine = UltronTradingEngine()

        // Start 24/7 Background Foreground Service (Brain #58)
        val serviceIntent = Intent(this, UltronBackgroundService::class.java)
        startService(serviceIntent)

        ultronSpeech.text = "আসসালামু আলাইকুম, বস! ৬৪টি সিস্টেম, ১০০টি ট্রেডিং ব্রেন এবং লাইফটাইম লার্নার ইঞ্জিন সক্রিয়। বলুন, কী করতে হবে?"

        // Voice Command Trigger Button
        micButton.setOnClickListener {
            startVoiceRecognition()
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD") // Bangla & English support
            putExtra(RecognizerIntent.EXTRA_PROMPT, "আল্ট্রনকে হুকুম দিন, বস...")
        }
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(this, "Voice recognition error!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)?.toLowerCase(Locale.ROOT) ?: ""
            
            processUltronSupremeCommand(spokenText)
        }
    }

    private fun processUltronSupremeCommand(command: String) {
        ultronSpeech.text = "হুকুম পেয়েছি, বস: \"$command\""

        when {
            // 1. Auto Trade Control (Stealth Mode)
            command.contains("auto trade on") -> {
                isAutoTradeActive = true
                securityBrain.activateStealthTradingMode(true)
                ultronSpeech.text = "STALTH MODE ON: Broker-safe anti-ban active. Ready for live execution with 1% risk."
            }
            command.contains("auto trade off") -> {
                isAutoTradeActive = false
                securityBrain.activateStealthTradingMode(false)
                ultronSpeech.text = "Trading Brain shifted to Analysis & Demo mode only."
            }
            command.contains("trading") || command.contains("quotex") || command.contains("binance") -> {
                // Execute 100 Trading Brains Analysis & Demo Check
                tradingEngine.executeAdvancedTradingAnalysis("EUR/USD / OTC Pair", isAutoTradeActive) { resultMessage ->
                    ultronSpeech.text = resultMessage
                    Toast.makeText(this, "Trading Brain Updated", Toast.LENGTH_LONG).show()
                }
            }

            // 2. Security & Ethical Hacking Shield (Brain #12)
            command.contains("security") || command.contains("scan") || command.contains("hacker") -> {
                val securityReport = securityBrain.executeGodModeProtection()
                ultronSpeech.text = securityReport
            }

            // 3. YouTube & Music Control
            command.contains("গান") || command.contains("youtube") || command.contains("play") -> {
                Toast.makeText(this, "YouTube কন্ট্রোল চালু হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                startActivity(intent)
            }

            // 4. WhatsApp Control
            command.contains("whatsapp") || command.contains("মেসেজ") -> {
                Toast.makeText(this, "WhatsApp ওপেন করা হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (intent != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "WhatsApp অ্যাপ পাওয়া যায়নি!", Toast.LENGTH_SHORT).show()
                }
            }

            // 5. Google Real-time Search Analysis
            command.contains("google") || command.contains("খুজো") || command.contains("search") -> {
                val query = command.replace("google", "").replace("খুজো", "").trim()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
                startActivity(intent)
            }

            // Default Assistant Reply
            else -> {
                ultronSpeech.text = "বস, আপনার কথা বুঝেছি। ৬৪টি সিস্টেম ও লাইফটাইম লার্নার এটি নিয়ে কাজ করছে। আরও বলুন!"
            }
        }
    }
}
