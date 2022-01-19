package com.sample.core.domain.repository

import com.sample.core.domain.entity.WeatherCityResponse
import io.reactivex.Single

interface WeatherItemRepository {

    fun getByCityName(cityName: String, apiKey: String): Single<WeatherCityResponse>

    fun getWeatherByLatLong(lat: Double?, lon: Double?, apiKey: String): Single<WeatherCityResponse>

}
