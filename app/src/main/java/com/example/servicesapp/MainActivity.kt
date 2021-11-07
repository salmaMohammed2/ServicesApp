package com.example.servicesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            Intent(this, RingtoneService::class.java).also { intent ->
                startService(intent)
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            Intent(this, RingtoneService::class.java).also { intent ->
                stopService(intent)
            }
        }

    }
}