package com.simpleweather.android

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.simpleweather.android.data.CityRepository
import com.simpleweather.android.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var weatherTextView: TextView
    private lateinit var fetchWeatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        weatherTextView = findViewById(R.id.weatherTextView)
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton)

        fetchWeatherButton.setOnClickListener {
            fetchWeather()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchWeather() {
        lifecycleScope.launch {
            try {
                val city = CityRepository.getCities().first()

                val response = RetrofitInstance.api.getCurrentWeather(
                    city.latitude,
                    city.longitude
                )

                val temperature = response.current.temperature
                weatherTextView.text = getString(
                    R.string.weather_format,
                    city.name, temperature
                )

            } catch (e: Exception) {
                weatherTextView.setText(R.string.weather_fetch_error)
                Log.e("MainActivity", "Error fetching data", e)
            }
        }
    }
}