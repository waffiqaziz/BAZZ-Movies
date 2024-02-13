package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.OMDb_API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OMDbApiConfig {
  fun getOMDBApiService(): OMDbApiService {
    val client = OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(OMDb_API_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .build()
    return retrofit.create(OMDbApiService::class.java)
  }

  companion object {
    fun getOMDBApiService(): OMDbApiService {
      val client = OkHttpClient.Builder()
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(OMDb_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
      return retrofit.create(OMDbApiService::class.java)
    }
  }
}