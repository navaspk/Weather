package com.sample.core.domain.usecase

import com.sample.core.domain.SingleUseCase
import com.sample.core.domain.executor.PostExecutionThread
import com.sample.core.domain.repository.WeatherItemRepository
import com.sample.core.extensions.safeGet
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.core.extensions.empty
import io.reactivex.Single
import javax.inject.Inject

class GetListWeatherByCityName @Inject constructor(
    private val getItemRepository: WeatherItemRepository,
    private val postExecutionThread: PostExecutionThread
) : SingleUseCase<WeatherCityResponse, GetListWeatherByCityName.Params>(
    postExecutionThread
) {

    override fun buildUseCase(params: Params?): Single<WeatherCityResponse> {
        return getItemRepository.getByCityName(
            params?.cityName.safeGet(),
            params?.apiKey.safeGet()
        )
    }

    data class Params constructor(
        val apiKey: String = String.empty,
        val cityName: String = String.empty
    ) {
        companion object {
            fun create(apiKey: String, cityName: String) =
                Params(
                    apiKey,
                    cityName
                )
        }
    }

}
