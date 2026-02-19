package com.simpleweather.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.simpleweather.android.data.City
import com.simpleweather.android.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class CityDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_city_detail)

        buildCityDetailedWeather()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.city_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun buildCityDetailedWeather() {
        val city = intent.getParcelableExtra("EXTRA_CITY", City::class.java) ?: return

        lifecycleScope.launch {
            val repository = WeatherRepository()
            val weatherDetail = repository.fetchWeatherForDetails(city)

            withContext(Dispatchers.Main) {
                weatherDetail?.let {
                    findViewById<TextView>(R.id.cityNameTextView).text = it.city.name
                    findViewById<TextView>(R.id.temperature).text =
                        getString(R.string.temperature_format, it.temperature)
                }
            }
        }
    }
}
