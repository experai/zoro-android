package com.experai.zoro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var modeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        modeSpinner = findViewById(R.id.modeSpinner)

        val modes = listOf(
            getString(R.string.mode_always),
            getString(R.string.mode_saver),
            getString(R.string.mode_privacy),
            getString(R.string.mode_headset),
            getString(R.string.mode_offline),
            getString(R.string.mode_standard)
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeSpinner.adapter = adapter

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val selected = modeSpinner.selectedItemPosition
            val mode = when (selected) {
                0 -> "always"
                1 -> "saver"
                2 -> "privacy"
                3 -> "headset"
                4 -> "offline"
                else -> "standard"
            }
            val intent = Intent(this, ZoroForegroundService::class.java).apply {
                action = ZoroForegroundService.ACTION_START
                putExtra("mode", mode)
            }
            startService(intent)
            statusText.text = getString(R.string.status_listening)
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            val intent = Intent(this, ZoroForegroundService::class.java).apply {
                action = ZoroForegroundService.ACTION_STOP
            }
            startService(intent)
            statusText.text = getString(R.string.status_idle)
        }
    }
}
