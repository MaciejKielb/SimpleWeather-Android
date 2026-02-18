package com.simpleweather.android.data

object CityRepository {
    private val cities = listOf(
        City("Slupsk", 54.4643, 17.0284),
        City("London", 51.5074, -0.1278),
        City("Berlin", 52.5200, 13.4050),
        City("Limassol", 34.6786, 33.0413),
        City("Nicosia",35.1856,33.3823)
    )

    fun getCities(): List<City> {
        return cities
    }
}