package com.simpleweather.android.network

import com.simpleweather.android.utils.readFileFromResources
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiServiceTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var api: WeatherApiService

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
        api = retrofit.create(WeatherApiService::class.java)
    }

    @Test
    fun `get current weather - returns correct data`() = runTest {

        val mockResponse =
            MockResponse().setBody(readFileFromResources("success_weather_response.json"))
                .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)

        val response = api.getCurrentWeather(
            latitude = 54.27,
            longitude = 17.01,
            current = "temperature_2m"
        )

        Assert.assertEquals(-8.9, response.current.temperature, 0.0)
    }
}