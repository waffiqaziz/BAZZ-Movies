package com.waffiq.bazz_movies.data.remote.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.BuildConfig.OMDb_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class OMDbApiConfig {
  fun getOMDBApiService(): OMDbApiService {
//    val loggingInterceptor =
//      HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
    val client = OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
//      .addInterceptor(loggingInterceptor)
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(OMDb_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()
    return retrofit.create(OMDbApiService::class.java)
  }

  companion object {
    fun getOMDBApiService(): OMDbApiService {
//      val loggingInterceptor =
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
      val client = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(OMDb_API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
      return retrofit.create(OMDbApiService::class.java)
    }
  }
}