package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.TMDB_API_URL
import com.waffiq.bazz_movies.utils.Constants.COUNTRY_API_LINK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CountryIPApiConfig {
  fun getApiService(): CountryIPApiService {
    val loggingInterceptor =
      HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(loggingInterceptor)
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(COUNTRY_API_LINK)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .build()
    return retrofit.create(CountryIPApiService::class.java)
  }

  companion object {
    fun getApiService(): CountryIPApiService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(COUNTRY_API_LINK)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
      return retrofit.create(CountryIPApiService::class.java)
    }
  }
}