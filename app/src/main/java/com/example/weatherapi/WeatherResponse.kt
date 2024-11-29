package com.example.weatherapi

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
) {
    data class Wind(
        val speed: Double
    )
}