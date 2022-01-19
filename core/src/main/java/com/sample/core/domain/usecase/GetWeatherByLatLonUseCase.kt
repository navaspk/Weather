package com.sample.core.domain.usecase

import com.sample.core.domain.SingleUseCase
import com.sample.core.domain.executor.PostExecutionThread
import com.sample.core.domain.repository.WeatherItemRepository
import com.sample.core.extensions.safeGet
import com.sample.core.domain.entity.WeatherCityResponse
import io.reactivex.Single
import javax.inject.Inject

class GetWeatherByLatLonUseCase @Inject constructor(
    private val getItemRepository: WeatherItemRepository,
    private val postExecutionThread: PostExecutionThread
) : SingleUseCase<WeatherCityResponse, GetWeatherByLatLonUseCase.Params>(
    postExecutionThread
) {

    override fun buildUseCase(params: Params?): Single<WeatherCityResponse> {
        return getItemRepository.getWeatherByLatLong(
            params?.lat,
            params?.lon,
            params?.apiKey.safeGet()
        )
    }

    data class Params constructor(
        val apiKey: String = "",
        val lat: Double? = 0.0,
        val lon: Double? = 0.0
    ) {
        companion object {
            fun create(apiKey: String, latitude: Double?, longitude: Double?) =
                Params(
                    apiKey,
                    latitude, longitude
                )
        }
    }

}
