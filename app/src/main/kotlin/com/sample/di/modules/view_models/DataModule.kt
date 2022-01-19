package com.sample.di.modules.view_models

import com.sample.core.di.scopes.PerApplication
import com.sample.core.domain.executor.PostExecutionThread
import com.sample.core.data.remote.GsonProvider
import com.sample.core.domain.repository.WeatherItemRepository
import com.sample.core.domain.repository.impl.WeatherItemRepositoryImpl
import com.sample.di.threadhelper.UiThread
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule {

    @Module
    companion object {
        @PerApplication
        @Provides
        @JvmStatic
        fun provideGson(): GsonProvider = GsonProvider()
    }

    @PerApplication
    @Binds
    abstract fun bindPostExecutionThread(uiThread: UiThread): PostExecutionThread

    @PerApplication
    @Binds
    abstract fun bindWeatherItemRepository(weatherItemRepository: WeatherItemRepositoryImpl): WeatherItemRepository

}