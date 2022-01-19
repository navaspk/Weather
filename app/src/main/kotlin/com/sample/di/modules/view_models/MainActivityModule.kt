package com.sample.di.modules.view_models

import com.sample.core.di.scopes.PerActivity
import com.sample.core.di.scopes.PerFragment
import com.sample.ui.main.activities.MainActivity
import com.sample.ui.main.fragment.home.HomeWeatherFragment
import com.sample.ui.main.fragment.favorite.WeatherSearchAndFavFragment
import com.sample.ui.main.fragment.independant.IndependentWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ActivityModule::class])
abstract class MainActivityModule {

    @PerActivity
    @Binds
    abstract fun bindMainActivity(mainActivity: MainActivity): MainActivity

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindHomeWeatherFragment(): HomeWeatherFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindWeatherSearchAndFavFragment(): WeatherSearchAndFavFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindIndependentWeatherFragment(): IndependentWeatherFragment

}