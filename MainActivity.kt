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
    private val SPEECH_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ultronSpeech = findViewById(R.id.ultronSpeech)
        val micButton = findViewById<ImageView>(R.id.micButton)

        ultronSpeech.text = "আসসালামু আলাইকুম, বস! আল্ট্রন ফুল পাওয়ারে অনলাইন। বলুন, কী করতে হবে?"

        // Voice Command Trigger
        micButton.setOnClickListener {
            startVoiceRecognition()
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD") // Bangla support
            putExtra(RecognizerIntent.EXTRA_PROMPT, "আল্ট্রনকে আপনার হুকুম জানান...")
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
            val spokenText = results?.get(0)?.lowercase(Locale.ROOT) ?: ""
            
            processUltronCommand(spokenText)
        }
    }

    private fun processUltronCommand(command: String) {
        ultronSpeech.text = "হুকুম পেয়েছি, বস: \"$command\""

        when {
            command.contains("গান") || command.contains("youtube") || command.contains("play") -> {
                // YouTube / Music Command Execution
                Toast.makeText(this, "YouTube থেকে গান চালানো হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                startActivity(intent)
            }
            command.contains("whatsapp") || command.contains("মেসেজ") -> {
                // WhatsApp Integration
                Toast.makeText(this, "WhatsApp ওপেন করা হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (intent != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "WhatsApp পাওয়া যায়নি!", Toast.LENGTH_SHORT).show()
                }
            }
            command.contains("google") || command.contains("খুজো") || command.contains("search") -> {
                // Google Real-time Search Analysis
                val query = command.replace("google", "").replace("খুজো", "").trim()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
                startActivity(intent)
            }
            command.contains("trading") || command.contains("quotex") || command.contains("binance") -> {
                // Trading Super Brain Access
                Toast.makeText(this, "VIP Trading Cabin & Stealth Mode Active!", Toast.LENGTH_LONG).show()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://quotex.com"))
                startActivity(intent)
            }
            else -> {
                // General AI response simulation
                Toast.makeText(this, "আল্ট্রন রিয়েল-টাইম বিশ্লেষণ সম্পন্ন করেছে।", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
