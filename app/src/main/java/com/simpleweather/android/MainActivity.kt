package com.simpleweather.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.simpleweather.android.data.City
import com.simpleweather.android.data.WeatherRepository
import kotlinx.coroutines.launch

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
            val repository = WeatherRepository()
            val weatherItems = repository.fetchWeatherForList()
            val container = findViewById<LinearLayout>(R.id.weatherContainer)

            weatherItems.forEachIndexed { index, item ->
                val rowView = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_weather, container, false)

                val cityNameTextView = rowView.findViewById<TextView>(R.id.cityNameTextView)
                val temperatureTextView = rowView.findViewById<TextView>(R.id.temperatureTextView)

                val locationTextView = rowView.findViewById<TextView>(R.id.locationTextView)

                cityNameTextView.text = item.city.name

                temperatureTextView.text = getString(R.string.temperature_format, item.temperature)

                if (index == 0) {
                    locationTextView.text = getString(R.string.my_location)
                    locationTextView.visibility = View.VISIBLE
                }
                rowView.setOnClickListener { navigateToCityDetailScreen(item.city) }

                container.addView(rowView)
            }
        }
    }

    private fun navigateToCityDetailScreen(city: City) {
        val intent = Intent(this, CityDetailActivity::class.java).putExtra("EXTRA_CITY", city)
        startActivity(intent)
    }
}