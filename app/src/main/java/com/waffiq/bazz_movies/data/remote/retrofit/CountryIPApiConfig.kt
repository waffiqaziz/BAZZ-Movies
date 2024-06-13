package com.waffiq.bazz_movies.data.remote.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.utils.common.Constants.COUNTRY_API_LINK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class CountryIPApiConfig {
  fun getApiService(): CountryIPApiService {
//    val loggingInterceptor =
//      HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()

    val client = OkHttpClient.Builder()
//      .addInterceptor(loggingInterceptor)
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(COUNTRY_API_LINK)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()
    return retrofit.create(CountryIPApiService::class.java)
  }

  companion object {
    fun   getApiService(): CountryIPApiService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
      val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(COUNTRY_API_LINK)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
      return retrofit.create(CountryIPApiService::class.java)
    }
  }
}