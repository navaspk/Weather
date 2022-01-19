package com.sample.di.modules.view_models

import androidx.lifecycle.ViewModel
import com.sample.di.key.ViewModelKey
import com.sample.ui.main.activities.MainActivityViewModel
import com.sample.ui.main.fragment.home.HomeWeatherFragmentViewModel
import com.sample.ui.main.fragment.favorite.WeatherSearchAndFavFragmentViewModel
import com.sample.ui.main.fragment.independant.IndependentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeWeatherFragmentViewModel::class)
    fun bindHomeWeatherFragmentViewModel(viewModel: HomeWeatherFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherSearchAndFavFragmentViewModel::class)
    fun bindWeatherSearchAndFavFragmentViewModel(viewModel: WeatherSearchAndFavFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IndependentViewModel::class)
    fun bindIndependentViewModel(viewModel: IndependentViewModel): ViewModel
}