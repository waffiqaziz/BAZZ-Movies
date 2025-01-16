package com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptorOMDb(private val apiKey: String) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val modifiedUrl = originalRequest.url.newBuilder()
      .addQueryParameter("apikey", apiKey)
      .build()
    val modifiedRequest = originalRequest.newBuilder()
      .url(modifiedUrl)
      .build()
    return chain.proceed(modifiedRequest)
  }
}

class ApiKeyInterceptorTMDB(private val apiKey: String) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val modifiedUrl = originalRequest.url.newBuilder()
      .addQueryParameter("api_key", apiKey)
      .build()
    val modifiedRequest = originalRequest.newBuilder()
      .url(modifiedUrl)
      .build()
    return chain.proceed(modifiedRequest)
  }
}
