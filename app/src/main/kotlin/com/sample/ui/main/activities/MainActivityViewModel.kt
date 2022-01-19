package com.sample.ui.main.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.base.BaseNavigator
import com.sample.base.BaseViewModel
import com.sample.core.domain.entity.WeatherCityResponse
import javax.inject.Inject

/**
 * As a Activity view model: In future we can used for write ultimate world logic
 *
 * And also act as shared view model: to share selected item between multiple fragment
 */
class MainActivityViewModel @Inject constructor() : BaseViewModel<BaseNavigator>() {

    var selectedCity: WeatherCityResponse? = null
    private val _queryString = MutableLiveData<String?>(null)
    val queryString: LiveData<String?> get() = _queryString

    fun getSearchResults(query: String?) {
        _queryString.value = query
    }

}
