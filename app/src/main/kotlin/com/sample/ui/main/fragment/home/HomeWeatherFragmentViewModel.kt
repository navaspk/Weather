package com.sample.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import com.sample.base.BaseViewModel
import com.sample.core.WeatherLogger
import com.sample.core.data.api.remote.BaseError
import com.sample.core.domain.rxcallback.OptimizedCallbackWrapper
import com.sample.core.domain.usecase.GetWeatherByLatLonUseCase
import com.sample.core.domain.entity.WeatherCityResponse
import com.sample.core.extensions.TAG
import javax.inject.Inject

/**
 * View model class responsible for performing ultimate logic & get back the response.
 * This is working with help of use case and repository classes
 */
class HomeWeatherFragmentViewModel @Inject constructor(
    private val weatherUseCase: GetWeatherByLatLonUseCase
) : BaseViewModel<HomeWeatherFragmentNavigator>() {

    // region VARIABLE
    var itemLiveData = MutableLiveData<WeatherCityResponse>()
    // endregion

    fun getCurrentLocationWeather(apiKey: String, latitude: Double?, longitude: Double?) {
        addDisposable(
            weatherUseCase.execute(
                CurrentWeatherSubscriber(),
                GetWeatherByLatLonUseCase.Params.create(apiKey, latitude, longitude)
            )
        )
    }

    inner class CurrentWeatherSubscriber : OptimizedCallbackWrapper<WeatherCityResponse>() {

        override fun onApiSuccess(response: WeatherCityResponse) {
            if (response.cod == 200) {
                itemLiveData.postValue(response)
            } else {
                getNavigator()?.somethingWentWrong()
            }
        }

        override fun onApiError(error: BaseError) {
            WeatherLogger.d(TAG,"response error is =${error}")
            getNavigator()?.somethingWentWrong()
        }

    }
}
