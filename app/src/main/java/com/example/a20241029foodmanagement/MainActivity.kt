package com.example.a20241029foodmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var foodApi: FoodApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        foodApi = ApiService.foodApi

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

            val foodEntry =
                FoodEntry(food_name = foodName, calories = foodCalories ?: 0, date = formattedDate)

            foodApi.addFood(foodEntry).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        displayFoodEntries(resultsTextView)
                    } else {
                        Snackbar.make(
                            findViewById(R.id.submit),
                            "Error: ${response.code()}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Snackbar.make(
                        findViewById(R.id.submit),
                        "Failed to add food entry: ${t.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
//                    Log.e("MainActivity", "Failed to add food entry: ${t.message}")
                }
            })
        }

        clearDBButton.setOnClickListener {
            foodApi.clearDatabase().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        displayFoodEntries(resultsTextView)
                    } else {
                        Snackbar.make(
                            findViewById(R.id.submit),
                            "Error: ${response.code()}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Snackbar.make(
                        findViewById(R.id.submit),
                        "Failed to clear database: ${t.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun displayTodayDate(dateView: TextView) {
        // Get today's date using Calendar
        val calendar = Calendar.getInstance()

        // Format the date to a string
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Customize the format as needed
        val formattedDate = dateFormat.format(calendar.time)

        // Set the formatted date string to the TextView
        dateView.text = "Date: $formattedDate"
    }

    private fun displayFoodEntries(textView: TextView) {
        foodApi.getTodaysFood().enqueue(object : Callback<List<FoodEntry>> {
            override fun onResponse(
                call: Call<List<FoodEntry>>,
                response: Response<List<FoodEntry>>
            ) {
                if (response.isSuccessful) {
                    val foodEntries =
                        response.body()?.joinToString("\n") { "${it.food_name}: ${it.calories}" }
                    textView.text = foodEntries ?: "No entries"
                } else {
                    Snackbar.make(
                        findViewById(R.id.results),
                        "Error: ${response.code()}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<FoodEntry>>, t: Throwable) {
                Snackbar.make(
                    findViewById(R.id.results),
                    "Failed to load food entries: ${t.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}