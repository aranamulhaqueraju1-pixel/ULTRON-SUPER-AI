package com.ultron.superai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvConsoleLog: TextView
    private lateinit var etCommandInput: EditText
    private lateinit var btnExecute: Button

    private val logBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvConsoleLog = findViewById(R.id.tvConsoleLog)
        etCommandInput = findViewById(R.id.etCommandInput)
        btnExecute = findViewById(R.id.btnExecute)

        // Initial futuristic welcome sequence
        appendLog("> ULTRON Core v2.0 Initialized.")
        appendLog("> AI Neural Network: ACTIVE.")
        appendLog("> Awaiting directives, Boss...")

        btnExecute.setOnClickListener {
            val command = etCommandInput.text.toString().trim()
            if (command.isNotEmpty()) {
                appendLog("\n> USER: $command")
                processAdvancedAICommand(command)
                etCommandInput.text.clear()
            }
        }
    }

    private fun processAdvancedAICommand(cmd: String) {
        when {
            cmd.equals("status", ignoreCase = true) -> {
                appendLog("[ULTRON AI]: All sub-routines operational. Neural link stable.")
            }
            cmd.equals("ai mode", ignoreCase = true) -> {
                appendLog("[ULTRON AI]: Advanced intelligence matrix online. Ready for custom modules.")
            }
            cmd.equals("clear", ignoreCase = true) -> {
                logBuilder.clear()
                appendLog("> Console buffer cleared.")
                tvConsoleLog.text = logBuilder.toString()
            }
            else -> {
                // Simulating intelligent processing response for Phase 2
                appendLog("[ULTRON AI]: Analyzing command parameters for '$cmd'...")
                appendLog("[ULTRON AI]: Intelligence engine executed successfully. Standing by for Phase 3 integration.")
            }
        }
    }

    private fun appendLog(text: String) {
        logBuilder.append(text).append("\n")
        tvConsoleLog.text = logBuilder.toString()
    }
}
