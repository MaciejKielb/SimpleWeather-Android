package com.simpleweather.android.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
): Parcelable
