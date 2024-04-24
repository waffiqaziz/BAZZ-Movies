package com.waffiq.bazz_movies.data.remote.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.BuildConfig.API_KEY
import com.waffiq.bazz_movies.BuildConfig.TMDB_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TMDBApiConfig {
  fun getApiService(): TMDBApiService {
//    val loggingInterceptor =
//      HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
    val client = OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(ApiKeyInterceptorTMDB(API_KEY))
//      .addInterceptor(loggingInterceptor)
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(TMDB_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()
    return retrofit.create(TMDBApiService::class.java)
  }

  companion object {
    fun getApiService(): TMDBApiService {
//      val loggingInterceptor =
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
      val client = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
        .addInterceptor(ApiKeyInterceptorTMDB(API_KEY))
        .build()
      val retrofit = Retrofit.Builder()
        .baseUrl(TMDB_API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
      return retrofit.create(TMDBApiService::class.java)
    }
  }
}