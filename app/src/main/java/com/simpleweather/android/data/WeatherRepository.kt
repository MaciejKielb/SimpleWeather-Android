package com.simpleweather.android.data

import android.util.Log
import com.simpleweather.android.network.RetrofitInstance
import kotlin.collections.map

data class WeatherDataItem(
    val city: City,
    val temperature: Double
)

class WeatherRepository {
    suspend fun fetchWeatherForList(): List<WeatherDataItem> {
        val cities: List<City> = CityRepository.getCities()

        return cities.map { city ->
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    latitude = city.latitude,
                    longitude = city.longitude
                )
                WeatherDataItem(city, response.current.temperature)

            } catch (e: Exception) {
                Log.e("WeatherRepository", "Failed to fetch weather", e)
                null
            }
        }.filterNotNull()
    }

    suspend fun fetchWeatherForDetails(city: City): WeatherDataItem? {
        try {
            val response = RetrofitInstance.api.getCurrentWeather(
                latitude = city.latitude,
                longitude = city.longitude
            )
            return WeatherDataItem(city, response.current.temperature)
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to fetch weather for ${city.name}", e)
            return null
        }
    }
}
