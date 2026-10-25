package com.ultron.superai

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
    // Apnar PC-er fix kora IP address ekhane bosano holo
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
                sendToServer(command, statusText)
            }
        }
    }

    private fun sendToServer(command: String, statusText: TextView) {
        executor.execute {
            try {
                // PC server-er sathe socket connection toiri
                val socket = Socket(pcIpAddress, port)
                val writer = PrintWriter(socket.outputStream, true)
                
                // Command transmit kora
                writer.println(command)
                
                writer.close()
                socket.close()

                runOnUiThread {
                    statusText.text = "STATUS: SENT [$command]"
                    statusText.setTextColor(0xFF00FF66.toInt()) // Green
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    statusText.text = "STATUS: CONNECTION FAILED"
                    statusText.setTextColor(0xFFFF3333.toInt()) // Red
                }
            }
        }
    }
}
