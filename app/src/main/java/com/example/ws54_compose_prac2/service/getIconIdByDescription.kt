package com.example.ws54_compose_prac2.service

import com.example.ws54_compose_prac2.R

object GetIconIdByDescription{
    val iconMap = mapOf(
        "sunny" to R.drawable.morning_sun_sunrise_icon,
        "rainy" to R.drawable.weather_clouds_rain_storm_thunder_icon,
        "cloudy" to R.drawable.cloudy_storm_wind_icon
    )

    fun getIconId(description: String): Int {
        return iconMap[description] ?: R.drawable.icon
    }
}