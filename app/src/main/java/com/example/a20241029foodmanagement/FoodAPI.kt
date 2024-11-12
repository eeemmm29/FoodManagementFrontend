package com.example.a20241029foodmanagement

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodAPI {
    @POST("/food")
    fun addFood(@Body foodEntry: FoodEntry): Call<Unit>

    @GET("/food/today")
    fun getTodaysFood(): Call<List<FoodEntry>>

    @GET("/food/summary")
    fun getCalorieSummary(): Call<String>

    @DELETE("/food")
    fun clearDatabase(): Call<Unit>
}   