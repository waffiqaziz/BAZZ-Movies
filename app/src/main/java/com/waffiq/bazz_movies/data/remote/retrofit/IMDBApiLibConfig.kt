package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.IMDbApiLib_API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class IMDBApiLibConfig {
  companion object {
    fun getIMDBLibApiService(): IMDBApiLibService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(IMDbApiLib_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
      return retrofit.create(IMDBApiLibService::class.java)
    }
  }
}