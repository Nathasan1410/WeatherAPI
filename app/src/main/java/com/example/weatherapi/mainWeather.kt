package com.example.weatherapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.moandjiezana.toml.Toml
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class mainWeather : Fragment(R.layout.fragment_main_weather) {

    private lateinit var cityInput: EditText
    private lateinit var cityText: TextView
    private lateinit var tempText: TextView
    private lateinit var descText: TextView
    private lateinit var refreshButton: Button
    private lateinit var feelsLikeText: TextView
    private lateinit var pressureText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView

    private var baseUrl: String? = null
    private var apiKey: String? = null
    private var units: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_weather, container, false)

        cityInput = view.findViewById(R.id.cityInput)
        cityText = view.findViewById(R.id.cityText)
        tempText = view.findViewById(R.id.tempText)
        descText = view.findViewById(R.id.descText)
        refreshButton = view.findViewById(R.id.refreshButton)
        feelsLikeText = view.findViewById(R.id.feelsLikeText)
        pressureText = view.findViewById(R.id.pressureText)
        humidityText = view.findViewById(R.id.humidityText)
        windSpeedText = view.findViewById(R.id.windSpeedText)

        loadApiConfig()
        fetchWeather()

        refreshButton.setOnClickListener {
            fetchWeather()
        }

        return view
    }

    private fun loadApiConfig() {
        try {
            val inputStream: InputStream = requireContext().assets.open("config.toml")
            val toml: Toml = Toml().read(inputStream)

            baseUrl = toml.getString("api.base_url")
            apiKey = toml.getString("api.api_key")
            units = toml.getString("api.units")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchWeather() {
        if (baseUrl == null || apiKey == null || units == null) {
            tempText.text = "Error: Missing API configurations"
            return
        }

        val city = cityInput.text.toString().ifEmpty { "Yogyakarta" }
        val weatherService = ApiClient.getClient(baseUrl!!).create(WeatherService::class.java)
        val call = weatherService.getWeather(city, apiKey!!, units!!)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()!!
                    val temp: String = "${weatherResponse.main.temp}°C"
                    val feelsLike: String = "${weatherResponse.main.feels_like}°C"
                    val pressure: String = "${weatherResponse.main.pressure} hPa"
                    val humidity: String = "${weatherResponse.main.humidity}%"
                    val windSpeed: String = "${weatherResponse.wind.speed} m/s"
                    val description: String = weatherResponse.weather[0].description

                    cityText.text = city
                    tempText.text = "Temp: $temp"
                    feelsLikeText.text = "Feels like: $feelsLike"
                    pressureText.text = "Pressure: $pressure"
                    humidityText.text = "Humidity: $humidity"
                    windSpeedText.text = "Wind speed: $windSpeed"
                    descText.text = "Description: $description"
                } else if (response.code() == 404) {
                    tempText.text = "City not found"
                    descText.text = ""
                    cityText.text = ""
                    feelsLikeText.text = ""
                    pressureText.text = ""
                    humidityText.text = ""
                    windSpeedText.text = ""
                    Toast.makeText(requireContext(), "City not found", Toast.LENGTH_SHORT).show()
                } else {
                    tempText.text = "Failed to fetch weather"
                    descText.text = ""
                    cityText.text = ""
                    feelsLikeText.text = ""
                    pressureText.text = ""
                    humidityText.text = ""
                    windSpeedText.text = ""
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tempText.text = "Failed to fetch weather"
                descText.text = ""
                cityText.text = ""
                feelsLikeText.text = ""
                pressureText.text = ""
                humidityText.text = ""
                windSpeedText.text = ""
            }
        })
    }
}