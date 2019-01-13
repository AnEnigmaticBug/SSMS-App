package org.bitspilani.ssms.messapp.screens.shared.data.retrofit.setup

import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val modifiedRequest = chain!!.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(modifiedRequest)
    }
}