package com.example.a20241029foodmanagement

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodApi {
    @POST("add_food")
    fun addFood(@Body food: FoodEntry): Call<Void>

    @GET("todays_food")
    fun getTodaysFood(): Call<List<FoodEntry>>

    @GET("calorie_summary")
    fun getCalorieSummary(): Call<List<Map<String, Any>>>

    @POST("clear_database")
    fun clearDatabase(): Call<Void>
}

object RetrofitClient {
    private const val BASE_URL = "https://aitmurzaevemir.pythonanywhere.com/food/" // Use your server's IP address

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiService {
    val foodApi: FoodApi by lazy {
        RetrofitClient.instance.create(FoodApi::class.java)
    }
}