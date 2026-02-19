package com.simpleweather.android.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class Temperature(
    @SerializedName("temperature_2m")
    val temperature: Double
)

data class WeatherData(
    @SerializedName("current")
    val current: Temperature
)

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m",
    ): WeatherData
}


