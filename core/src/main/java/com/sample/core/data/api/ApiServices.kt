package com.sample.core.data.api

import com.sample.core.domain.entity.WeatherCityResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
    fun getWeatherByCityName(
        @Query("q") queryString: String,
        @Query("appid") key: String
    ): Single<WeatherCityResponse>

    @GET("data/2.5/weather")
    fun getWeatherByLatLon(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("appid") key: String
    ): Single<WeatherCityResponse>

}
