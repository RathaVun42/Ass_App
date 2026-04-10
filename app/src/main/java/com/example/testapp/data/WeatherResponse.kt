package com.example.testapp.data


data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys
)
data class GeoResponseItem(
    val name: String,
    val country: String
)
data class Main(
    val temp: Double,
    val humidity: Int,
    val feels_like: Double,

)
data class Weather(
    val main: String,
    val description: String
)
data class Wind(
    val speed: Double
)
data class Sys(
    val country: String
)
