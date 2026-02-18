package com.simpleweather.android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.simpleweather.android.data.CityRepository
import com.simpleweather.android.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buildWeatherList()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun buildWeatherList() {
        lifecycleScope.launch {
            val container = findViewById<LinearLayout>(R.id.weatherContainer)
            val cities = CityRepository.getCities()

            cities.forEachIndexed { index, city ->
                try {
                    val response = RetrofitInstance.api.getCurrentWeather(
                        latitude = city.latitude,
                        longitude = city.longitude
                    )

                    withContext(Dispatchers.Main) {
                        val rowView = LayoutInflater.from(this@MainActivity)
                            .inflate(R.layout.item_weather, container, false)

                        val cityNameTextView = rowView.findViewById<TextView>(R.id.cityNameTextView)
                        val temperatureTextView =
                            rowView.findViewById<TextView>(R.id.temperatureTextView)

                        val locationTextView = rowView.findViewById<TextView>(R.id.locationTextView)

                        cityNameTextView.text = city.name
                        temperatureTextView.text =
                            getString(R.string.temperature_format, response.current.temperature)

                        if (index == 0) {
                            locationTextView.text = getString(R.string.my_location)
                            locationTextView.visibility = View.VISIBLE
                        }

                        container.addView(rowView)
                    }

                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching weather for ${city.name}", e)
                }
            }
        }
    }
}