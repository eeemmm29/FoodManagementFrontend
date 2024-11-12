package com.example.a20241029foodmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        dbHelper = DatabaseHelper(this)
        val summaryTextView = findViewById<TextView>(R.id.historyTextView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Display calorie summary
        displayCalorieSummary(summaryTextView)

        // Set up the back button click listener
        backButton.setOnClickListener {
            finish() // This will close the current activity and return to the previous one
        }
    }

    private fun displayCalorieSummary(textView: TextView) {
        val calorieSummary = dbHelper.getCalorieSummary()
        textView.text = calorieSummary
    }
}