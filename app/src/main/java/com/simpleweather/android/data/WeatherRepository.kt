package com.simpleweather.android.data

import android.util.Log
import com.simpleweather.android.network.RetrofitInstance
import kotlin.collections.map

data class WeatherListItem(
    val city: City,
    val temperature: Double
)

data class WeatherDetailItem(
    val city: City,
    val temperature: Double,
    val weatherCode: Int?,
    val temperatureMin: Double?,
    val temperatureMax: Double?,
    val windSpeed: Double?
)

class WeatherRepository {
    suspend fun fetchWeatherForList(): List<WeatherListItem> {
        val cities: List<City> = CityRepository.getCities()

        return cities.map { city ->
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    latitude = city.latitude,
                    longitude = city.longitude,
                    current = "temperature_2m"
                )
                WeatherListItem(city, response.current.temperature)

            } catch (e: Exception) {
                Log.e("WeatherRepository", "Failed to fetch weather", e)
                null
            }
        }.filterNotNull()
    }

    suspend fun fetchWeatherForDetails(city: City): WeatherDetailItem? {
        try {
            val response = RetrofitInstance.api.getCurrentWeather(
                latitude = city.latitude,
                longitude = city.longitude,
                current = "temperature_2m,weather_code,wind_speed_10m",
                daily = "temperature_2m_max,temperature_2m_min",
                forecastDays = 1
            )

            return with(response) {
                WeatherDetailItem(
                    city,
                    current.temperature,
                    current.weatherCode,
                    daily?.temperatureMin?.firstOrNull(),
                    daily?.temperatureMax?.firstOrNull(),
                    current.windSpeed
                )
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to fetch weather for ${city.name}", e)
            return null
        }
    }
}
