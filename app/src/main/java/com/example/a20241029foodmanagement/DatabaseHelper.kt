package com.example.a20241029foodmanagement

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FoodManagement.db"
        private const val DATABASE_VERSION = 2 // Increment the version
        const val TABLE_NAME = "Food"
        const val COLUMN_ID = "id"
        const val COLUMN_FOOD_NAME = "food_name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_DATE = "date" // New column for date
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FOOD_NAME TEXT NOT NULL,
                $COLUMN_CALORIES INTEGER NOT NULL,
                $COLUMN_DATE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Method to add a food entry
    fun insertFood(foodName: String, calories: Int?, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FOOD_NAME, foodName)
            put(COLUMN_CALORIES, calories)
            put(COLUMN_DATE, date) // Store the date
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Method to get today's food entries
    fun getTodaysFood(today: String): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME, null, "$COLUMN_DATE = ?", arrayOf(today), null, null, null)
    }

    fun getCalorieSummary(): String {
        val db = this.readableDatabase
        val summary = StringBuilder()

        val cursor = db.rawQuery("""
        SELECT date, SUM(calories) as totalCalories 
        FROM $TABLE_NAME 
        GROUP BY date
    """.trimIndent(), null)

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val totalCalories = cursor.getInt(cursor.getColumnIndexOrThrow("totalCalories"))
                summary.append("[$date] Total Calories: $totalCalories\n")
            } while (cursor.moveToNext())
        }
        cursor.close()

        return summary.toString().ifEmpty { "No entries found." }
    }


    fun clearDatabase() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_NAME}")
        db.close()
    }
}
