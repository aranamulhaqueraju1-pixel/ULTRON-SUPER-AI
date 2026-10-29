package com.ultron.superai

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()
    private val pcIpAddress = "192.168.1.108" // পিসির লোকাল আইপি
    private val port = 65000

    private lateinit var tvStatus: TextView
    private lateinit var txtPair: TextView
    private lateinit var txtWinRate: TextView
    private lateinit var etCommand: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- ULTRON BACKGROUND SERVICE START ---
        try {
            val serviceIntent = Intent(this, UltronBackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
            Log.d("UltronAI", "Background Service Triggered Successfully")
        } catch (e: Exception) {
            Log.e("UltronAI", "Failed to start service: ${e.message}")
        }

        tvStatus = findViewById(R.id.tvStatus)
        txtPair = findViewById(R.id.txtPair)
        txtWinRate = findViewById(R.id.txtWinRate)
        etCommand = findViewById(R.id.etCommand)
        val btnExecute = findViewById<Button>(R.id.btnExecute)
        val btnGetSignal = findViewById<Button>(R.id.btnGetSignal)
        val btnDoctor = findViewById<Button>(R.id.btnDoctor)
        val btnPentest = findViewById<Button>(R.id.btnPentest)

        // Execute Button Click
        btnExecute.setOnClickListener {
            executeCommandFromInput()
        }

        // Keyboard Enter Key Listener (ইনপুট বক্সে এন্টার চাপলে অটো কমান্ড এক্সিকিউট হবে)
        etCommand.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                executeCommandFromInput()
                true
            } else {
                false
            }
        }

        // Quick Shortcut Buttons
        btnGetSignal.setOnClickListener { processAndSendCommand("get_signal") }
        btnDoctor.setOnClickListener { processAndSendCommand("doctor") }
        btnPentest.setOnClickListener { processAndSendCommand("pentest") }
    }

    private fun executeCommandFromInput() {
        val command = etCommand.text.toString().trim()
        if (command.isNotEmpty()) {
            processAndSendCommand(command)
        }
    }

    private fun processAndSendCommand(command: String) {
        // Local Command Check
        when (command.lowercase()) {
            "clear" -> {
                tvStatus.text = "[ SYSTEM ONLINE ]\nConsole cleared by BOSS."
                tvStatus.setTextColor(Color.parseColor("#00E5FF"))
                txtPair.text = "PAIR: ---"
                txtWinRate.text = "WIN RATE: --%"
                etCommand.text.clear()
                return
            }
            "ai:status" -> {
                tvStatus.text = "[ STATUS: ULTRON AI CORE ONLINE ]\n64 Systems, Scraper & Hacking Modules Active."
                tvStatus.setTextColor(Color.parseColor("#00FF66"))
                etCommand.text.clear()
                return
            }
        }

        tvStatus.text = "STATUS: PROCESSING [$command]..."
        tvStatus.setTextColor(Color.parseColor("#FFEA00"))
        etCommand.text.clear()

        sendToServer(command)
    }

    private fun sendToServer(command: String) {
        executor.execute {
            var socket: Socket? = null
            try {
                // সকেট কানেকশন উইথ ৩ সেকেন্ড টাইমআউট (অটো-লক প্রিভেন্ট করতে)
                socket = Socket()
                socket.connect(InetSocketAddress(pcIpAddress, port), 3000)
                
                val writer = PrintWriter(socket.outputStream, true)
                val reader = BufferedReader(InputStreamReader(socket.inputStream))
                
                writer.println(command)
                val response = reader.readLine()
                
                writer.close()
                reader.close()
                socket.close()

                runOnUiThread {
                    if (response != null) {
                        try {
                            // Try parsing JSON Trading Signal
                            val json = JSONObject(response)
                            val pair = json.optString("pair", "N/A")
                            val action = json.optString("action", "N/A")
                            val winRate = json.optString("win_rate", "N/A")
                            val intel = json.optString("market_intel", json.optString("intel", ""))

                            txtPair.text = "PAIR: $pair"
                            txtWinRate.text = "WIN RATE: $winRate"

                            val report = "[ VIP SIGNAL RECEIVED ]\nAsset: $pair\nAction: $action\nWin Prob: $winRate\nIntel: $intel"
                            tvStatus.text = report
                            tvStatus.setTextColor(Color.parseColor("#00FF66"))
                        } catch (e: Exception) {
                            // Non-JSON response (Doctor/Pentest)
                            tvStatus.text = "[ EXECUTION RESULT ]\n$response"
                            tvStatus.setTextColor(Color.parseColor("#64FFDA"))
                        }
                    } else {
                        tvStatus.text = "STATUS: NO RESPONSE FROM BRIDGE"
                        tvStatus.setTextColor(Color.parseColor("#FF3333"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    tvStatus.text = "STATUS: CONNECTION FAILED\nMake sure PC bridge is running at $pcIpAddress:$port\n(${e.localizedMessage})"
                    tvStatus.setTextColor(Color.parseColor("#FF3333"))
                }
            } finally {
                try {
                    socket?.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
}
