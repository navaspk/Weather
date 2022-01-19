package com.sample.ui.main.fragment.favorite

import com.sample.base.BaseViewModel
import com.sample.core.WeatherLogger
import com.sample.core.data.api.remote.BaseError
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.core.domain.rxcallback.OptimizedCallbackWrapper
import com.sample.core.domain.usecase.GetListWeatherByCityName
import com.sample.core.extensions.TAG
import javax.inject.Inject

/**
 * View model class for search by city name
 */
class WeatherSearchAndFavFragmentViewModel @Inject constructor(
    private val useCase: GetListWeatherByCityName
) : BaseViewModel<CityNameNavigator>() {

    fun searchCityByName(query: String, apiKey: String) {

        addDisposable(
            useCase.execute(
                CurrentWeatherSubscriber(),
                GetListWeatherByCityName.Params.create(apiKey, query)
            )
        )

    }

    inner class CurrentWeatherSubscriber : OptimizedCallbackWrapper<WeatherCityResponse>() {

        override fun onApiSuccess(response: WeatherCityResponse) {
            response.cod?.takeIf { it == 200 }?.apply {
                getNavigator()?.onSearchCityNameFound(response)
            } ?: getNavigator()?.onSearchCityNotFound()
        }

        override fun onApiError(error: BaseError) {
            WeatherLogger.d(TAG, "response error is =${error}")
            getNavigator()?.onSearchCityNotFound()
        }

    }

}