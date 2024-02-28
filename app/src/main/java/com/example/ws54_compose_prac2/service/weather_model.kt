package com.example.ws54_compose_prac2.service

data class WeatherResponse(
    val Taichung:taichung,
    val Taipei:taipei,
    val Ayo:ayo,
    val TestCity:testcity,
    val Yee:yee
)

data class taichung(
    val current:Current,
    val forecast:Forecast
)

data class taipei(
    val current: Current,
)
data class ayo(
    val current: Current,
)
data class testcity(
    val current: Current,
)
data class yee(
    val current: Current,
)
// ---------------------------------------------
data class Current(
    val temp_c: Int,
    val temp_f: Double,
    val maxTemp_c:Int,
    val minTemp_c:Int,
    val maxTemp_f:Double,
    val minTemp_f: Double,
    val uv:Double,
    val pm25:Double,
    val daily_chance_of_rain:Int,
    val description: String
)

data class Forecast(
    val day:List<DayData>,
    val hourly:List<HourData>
)

data class DayData(
    val date:String,
    val maxTemp_c: Int,
    val maxTemp_f: Double,
    val minTemp_c: Int,
    val mixTemp_f:Double,
    val daily_chance_of_rain:Int,
    val description: String
)

data class HourData(
    val time:String,
    val temp_c:Int,
    val temp_f:Double,
    val daily_chance_of_rain: Int,
    val description: String
)