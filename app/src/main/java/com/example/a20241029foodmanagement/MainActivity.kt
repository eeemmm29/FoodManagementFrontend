package com.example.a20241029foodmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val foodNameEdit = findViewById<EditText>(R.id.foodNameEdit)
        val foodCaloriesEdit = findViewById<EditText>(R.id.foodCaloriesEdit)
        val submitButton = findViewById<Button>(R.id.submitButton)

        val dateView = findViewById<TextView>(R.id.dateView)
        displayTodayDate(dateView)
        val resultsTextView = findViewById<TextView>(R.id.resultsTextView)

        val historyButton = findViewById<Button>(R.id.historyButton)

        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        val clearDBButton = findViewById<Button>(R.id.clearDBButton)

        displayFoodEntries(resultsTextView)

        submitButton.setOnClickListener {
            val foodName = foodNameEdit.text.toString()
            val foodCalories = foodCaloriesEdit.text.toString().toIntOrNull()

            // Get today's date
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            // Insert food with today's date
            dbHelper.insertFood(foodName, foodCalories, formattedDate)

            displayFoodEntries(resultsTextView)
        }

        clearDBButton.setOnClickListener {
            dbHelper.clearDatabase()
            displayFoodEntries(resultsTextView)
        }
    }

    private fun displayTodayDate(dateView: TextView) {
        // Get today's date using Calendar
        val calendar = Calendar.getInstance()

        // Format the date to a string
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Customize the format as needed
        val formattedDate = dateFormat.format(calendar.time)

        // Set the formatted date string to the TextView
        dateView.text = "Date: $formattedDate"
    }

    private fun displayFoodEntries(textView: TextView) {
        // Get today's date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        // Get only today's food entries
        val cursor = dbHelper.getTodaysFood(formattedDate)
        val foodEntries = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val foodName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_NAME))
                val calories = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIES))
                foodEntries.append("$foodName: $calories\n")
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Set the formatted string in the TextView
        textView.text = foodEntries.toString()
    }
}