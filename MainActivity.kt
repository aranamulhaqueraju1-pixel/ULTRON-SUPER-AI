package com.ultron.superai

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()
    // পিসির সঠিক লোকাল আইপি অ্যাড্রেস
    private val pcIpAddress = "192.168.1.108"
    private val port = 65000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText = findViewById<TextView>(R.id.statusText)
        val commandInput = findViewById<EditText>(R.id.commandInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val command = commandInput.text.toString().trim()
            
            if (command.isNotEmpty()) {
                // 1. Local Command Handling (Phase 2 Logic: clear & ai:status)
                when (command.lowercase()) {
                    "clear" -> {
                        statusText.text = "STATUS: CONSOLE CLEARED"
                        statusText.setTextColor(Color.parseColor("#00E5FF")) // Cyan color
                        commandInput.text.clear()
                        return@setOnClickListener
                    }
                    "ai:status" -> {
                        statusText.text = "STATUS: ULTRON AI CORE ONLINE [READY]"
                        statusText.setTextColor(Color.parseColor("#00FF66")) // Green color
                        commandInput.text.clear()
                        return@setOnClickListener
                    }
                }

                // 2. Real-time Processing Feedback for Network/Server Commands
                statusText.text = "STATUS: PROCESSING AI COMMAND..."
                statusText.setTextColor(Color.parseColor("#FFEA00")) // Yellow color
                
                // Auto-clear input box immediately
                commandInput.text.clear()

                // 3. Socket Transmission to PC Bridge
                sendToServer(command, statusText)
            }
        }
    }

    private fun sendToServer(command: String, statusText: TextView) {
        executor.execute {
            try {
                // পিসির সার্ভারের সাথে সকেট কানেকশন তৈরি
                val socket = Socket(pcIpAddress, port)
                val writer = PrintWriter(socket.outputStream, true)
                
                // কমান্ড ট্রান্সমিট করা
                writer.println(command)
                
                writer.close()
                socket.close()

                // সফলভাবে সেন্ড হওয়ার পর UI আপডেট
                runOnUiThread {
                    statusText.text = "STATUS: EXECUTED [$command]"
                    statusText.setTextColor(Color.parseColor("#00FF66")) // Green
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // কানেকশন ফেইল করলে UI আপডেট
                runOnUiThread {
                    statusText.text = "STATUS: CONNECTION FAILED"
                    statusText.setTextColor(Color.parseColor("#FF3333")) // Red
                }
            }
        }
    }
}
