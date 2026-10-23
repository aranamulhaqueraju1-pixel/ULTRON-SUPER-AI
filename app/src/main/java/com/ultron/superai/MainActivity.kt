package com.ultron.superai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val etCommand = findViewById<EditText>(R.id.etCommand)
        val btnExecute = findViewById<Button>(R.id.btnExecute)

        btnExecute.setOnClickListener {
            val command = etCommand.text.toString()
            if (command.isNotEmpty()) {
                tvStatus.append("\n> $command\n[ULTRON]: Processing command, Boss...")
                etCommand.text.clear()
            } else {
                tvStatus.append("\n[ULTRON]: Please give a command, Boss.")
            }
        }
    }
}
