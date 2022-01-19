package com.sample.core.domain.rxcallback

import com.sample.core.data.api.remote.BaseError

interface OptimizedResponseCallBack<T> {
    fun onApiSuccess(response: T)
    fun onApiError(error: BaseError)
}
