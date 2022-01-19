package com.sample.ui.main.fragment.favorite

import com.sample.base.BaseNavigator
import com.sample.core.domain.entity.WeatherCityResponse

interface CityNameNavigator: BaseNavigator {

    fun onSearchCityNameFound(searchedResult: WeatherCityResponse)

    fun onSearchCityNotFound()
}