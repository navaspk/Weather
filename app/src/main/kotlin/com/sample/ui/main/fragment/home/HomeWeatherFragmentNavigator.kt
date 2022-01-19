package com.sample.ui.main.fragment.home

import com.sample.base.BaseNavigator

interface HomeWeatherFragmentNavigator: BaseNavigator {

    fun somethingWentWrong(responseError: Boolean = false)

}
