package com.sample.di.modules.view_models

import com.sample.core.di.scopes.PerApplication
import com.sample.core.data.remote.NetworkUtil
import com.sample.core.data.api.ApiServices
import dagger.Module
import dagger.Provides

@Module
class RemoteModule {

    @Module
    companion object {

        @PerApplication
        @Provides
        @JvmStatic
        fun provideApiService(networkUtil: NetworkUtil) =
            networkUtil.create(ApiServices::class.java)

    }

}