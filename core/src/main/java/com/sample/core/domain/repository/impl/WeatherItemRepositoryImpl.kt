package com.sample.core.domain.repository.impl

import com.sample.core.data.api.ApiServices
import com.sample.core.domain.repository.WeatherItemRepository
import com.sample.core.domain.entity.WeatherCityResponse
import io.reactivex.Single
import javax.inject.Inject

class WeatherItemRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices
) : WeatherItemRepository {

    override fun getByCityName(
        cityName: String,
        apiKey: String
    ): Single<WeatherCityResponse> {
        return apiServices.getWeatherByCityName(cityName, apiKey)
    }

    override fun getWeatherByLatLong(
        lat: Double?,
        lon: Double?,
        apiKey: String
    ): Single<WeatherCityResponse> {
        return apiServices.getWeatherByLatLon(lat, lon, apiKey)
    }
}
