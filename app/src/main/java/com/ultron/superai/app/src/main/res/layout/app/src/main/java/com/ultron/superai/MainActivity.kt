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
    private lateinit var tvStatusBar: TextView

    // Simulated Autonomous Trading Memory & Stats
    private var lastNightTradesCount: Int = 14
    private var lastNightAccuracy: Double = 87.5
    private var selfLearnedRulesCount: Int = 105 // 100 base + 5 new self-learned rules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI Components
        tvConsoleLog = findViewById(R.id.tvConsoleLog)
        etCommandInput = findViewById(R.id.etCommandInput)
        btnExecute = findViewById(R.id.btnExecute)
        tvStatusBar = findViewById(R.id.tvStatusBar)

        // Initial System Boot Log
        appendLog("> ULTRON SUPER AI: Online.")
        appendLog("> Neural Link: Stable. 64 Sub-systems Armed.")
        appendLog("> Base Protocols: 100 Active (Dynamic Learning Enabled).\n")

        // Execute Button Click Listener
        btnExecute.setOnClickListener {
            val command = etCommandInput.text.toString().trim()
            if (command.isNotEmpty()) {
                processUserCommand(command)
                etCommandInput.text.clear()
            }
        }
    }

    private fun processUserCommand(input: String) {
        appendLog("Boss: $input")

        val lowerInput = input.lowercase()

        val response = when {
            lowerInput.contains("trade") || lowerInput.contains("trading") || lowerInput.contains("result") -> {
                "ULTRON: Boss, last night I executed $lastNightTradesCount demo trades autonomously. Accuracy was at $lastNightAccuracy%. I integrated 5 new rules from web intelligence into my core database (Total active rules: $selfLearnedRulesCount)."
            }
            lowerInput.contains("status") -> {
                "ULTRON: All 64 sub-systems operating at peak performance. Autonomous engine is on standby."
            }
            lowerInput.contains("clear") -> {
                tvConsoleLog.text = "> Console Cleared.\n"
                return
            }
            else -> {
                "ULTRON: Command acknowledged: '$input'. Processing via neural matrix..."
            }
        }

        appendLog("$response\n")
    }

    private fun appendLog(text: String) {
        tvConsoleLog.append("$text\n")
    }
}
