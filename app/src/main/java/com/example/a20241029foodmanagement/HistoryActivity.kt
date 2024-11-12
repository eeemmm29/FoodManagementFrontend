package com.example.a20241029foodmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var foodApi: FoodApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        foodApi = ApiService.foodApi

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
        foodApi.getCalorieSummary().enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    val summaryList = response.body()
                    if (!summaryList.isNullOrEmpty()) {
                        val summaryText = summaryList.joinToString(separator = "\n") { entry ->
                            val date = entry["date"] as? String ?: "Unknown date"
                            val totalCalories = entry["totalCalories"] as? Double ?: 0.0
                            "$date: ${totalCalories.toInt()} calories"
                        }
                        textView.text = summaryText
                    } else {
                        textView.text = "No summary available"
                    }
                } else {
                    textView.text = "Error fetching summary"
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                textView.text = "Error fetching summary"
            }
        })
    }
}