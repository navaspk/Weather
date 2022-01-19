package com.sample.di.modules.view_models


import com.sample.core.di.scopes.PerApplication
import com.sample.core.data.remote.GsonProvider
import com.sample.core.data.remote.NetworkUtil
import com.weather.sample.BuildConfig
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    @PerApplication
    fun provideNetworkService(
        gsonProvider: GsonProvider
    ) = NetworkUtil(
        gsonProvider = gsonProvider,
        endPoint = BuildConfig.BASE_URL
    )
}