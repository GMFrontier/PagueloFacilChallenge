package com.gmfrontier.transaction_data.util

import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

const val HEADER_AUTHORIZATION = "Authorization"

class TokenInterceptor @Inject constructor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val accessToken = "brEyQRSzMm2UwQa5v0NsobRa3U8nH5xT|DIRbLgLJzZecjdBqotmtQQXfB"
        originalRequest.newBuilder()
            .addHeader(HEADER_AUTHORIZATION, accessToken)
            .build()
        val response = chain.proceed(originalRequest)
        return try {
            response
        } catch (error: SocketTimeoutException) {
            chain.proceed(chain.request())
        }
    }
}