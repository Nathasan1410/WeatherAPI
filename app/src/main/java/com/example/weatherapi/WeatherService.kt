package com.example.weatherapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<WeatherResponse>
}
