package com.ultron.superai

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.PrintWriter
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var ultronSpeech: TextView
    private lateinit var ivCentralBrain: ImageView
    private lateinit var tvDailyLog: TextView
    private lateinit var securityBrain: UltronSecurityBrain
    private lateinit var tradingEngine: UltronTradingEngine
    
    private val SPEECH_REQUEST_CODE = 100
    private var isAutoTradeActive = false

    private val executor = Executors.newSingleThreadExecutor()
    private val pcIpAddress = "192.168.1.108"
    private val port = 65000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ultronSpeech = findViewById(R.id.ultronSpeech)
        ivCentralBrain = findViewById(R.id.ivCentralBrain)
        tvDailyLog = findViewById(R.id.tvDailyLog)
        val micButton = findViewById<ImageView>(R.id.micButton)

        startBrainRotation()

        securityBrain = UltronSecurityBrain(this)
        tradingEngine = UltronTradingEngine()

        val serviceIntent = Intent(this, UltronBackgroundService::class.java)
        startService(serviceIntent)

        appendLearningLog(this, "System Boot: ৬৪টি সিস্টেম, ১০০টি ট্রেডিং ব্রেন ও ডেইলি মনিটরিং সক্রিয়।")
        tvDailyLog.text = readLearningLog(this)

        ultronSpeech.text = "আসসালামু আলাইকুম, বস! ৬৪টি সিস্টেম, ১০০টি ট্রেডিং ব্রেন, পিসি সকেট ও ব্যাকগ্রাউন্ড সার্ভিস সক্রিয়। বলুন, কী করতে হবে?"

        micButton.setOnClickListener {
            startVoiceRecognition()
        }
    }

    private fun startBrainRotation() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 4000
            repeatCount = Animation.INFINITE
            interpolator = LinearInterpolator()
        }
        ivCentralBrain.startAnimation(rotateAnimation)
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
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
            val spokenText = results?.get(0)?.lowercase(Locale.ROOT) ?: ""
            processUltronSupremeCommand(spokenText)
        }
    }

    private fun processUltronSupremeCommand(command: String) {
        ultronSpeech.text = "হুকুম পেয়েছি, বস: \"$command\""
        appendLearningLog(this, "Voice Command Executed: $command")

        when {
            command.contains("auto trade on") -> {
                isAutoTradeActive = true
                securityBrain.activateStealthTradingMode(true)
                ultronSpeech.text = "STEALTH MODE ON: Broker-safe anti-ban active. Ready for live execution with 1% risk."
                transmitCommandToPC("auto_trade_on")
            }
            command.contains("auto trade off") -> {
                isAutoTradeActive = false
                securityBrain.activateStealthTradingMode(false)
                ultronSpeech.text = "Trading Brain shifted to Analysis & Demo mode only."
                transmitCommandToPC("auto_trade_off")
            }
            command.contains("trading") || command.contains("quotex") || command.contains("binance") -> {
                tradingEngine.executeAdvancedTradingAnalysis("EUR/USD / OTC Pair", isAutoTradeActive) { resultMessage ->
                    ultronSpeech.text = resultMessage
                    Toast.makeText(this, "Trading Brain Updated", Toast.LENGTH_LONG).show()
                }
                transmitCommandToPC(command)
            }
            command.contains("security") || command.contains("scan") || command.contains("hacker") -> {
                val securityReport = securityBrain.executeGodModeProtection()
                ultronSpeech.text = securityReport
                transmitCommandToPC(command)
            }
            command.contains("গান") || command.contains("youtube") || command.contains("play") -> {
                Toast.makeText(this, "YouTube কন্ট্রোল চালু হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                startActivity(intent)
                transmitCommandToPC(command)
            }
            command.contains("whatsapp") || command.contains("মেসেজ") -> {
                Toast.makeText(this, "WhatsApp ওপেন করা হচ্ছে...", Toast.LENGTH_SHORT).show()
                val intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (intent != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "WhatsApp অ্যাপ পাওয়া যায়নি!", Toast.LENGTH_SHORT).show()
                }
                transmitCommandToPC(command)
            }
            command.contains("google") || command.contains("খুজো") || command.contains("search") -> {
                val query = command.replace("google", "").replace("খুজো", "").trim()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
                startActivity(intent)
                transmitCommandToPC(command)
            }
            else -> {
                ultronSpeech.text = "বস, আপনার কথা বুঝেছি। ৬৪টি সিস্টেম ও পিসি সার্ভার এটি নিয়ে কাজ করছে।"
                transmitCommandToPC(command)
            }
        }
    }

    private fun transmitCommandToPC(command: String) {
        executor.execute {
            try {
                val socket = Socket(pcIpAddress, port)
                val writer = PrintWriter(socket.outputStream, true)
                writer.println(command)
                writer.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun appendLearningLog(context: Context, newEntry: String) {
        val fileName = "ultron_learning_log.txt"
        val file = File(context.filesDir, fileName)
        val currentDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        val logData = "[$currentDate] - $newEntry\n"
        try {
            file.appendText(logData)
            if (::tvDailyLog.isInitialized) {
                tvDailyLog.text = readLearningLog(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readLearningLog(context: Context): String {
        val fileName = "ultron_learning_log.txt"
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            file.readText()
        } else {
            "কোনো লার্নিং ডাটা পাওয়া যায়নি।"
        }
    }
}
