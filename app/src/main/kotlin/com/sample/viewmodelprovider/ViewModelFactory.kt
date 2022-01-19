package com.sample.viewmodelprovider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.core.WeatherLogger
import com.sample.core.di.scopes.PerApplication
import com.sample.core.extensions.safeGet
import javax.inject.Inject
import javax.inject.Provider

@PerApplication
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown ViewModel: '$modelClass'")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            WeatherLogger.e(ViewModelFactory::class.java.simpleName, e.message.safeGet())
            throw  RuntimeException(e)
        }
    }
}

