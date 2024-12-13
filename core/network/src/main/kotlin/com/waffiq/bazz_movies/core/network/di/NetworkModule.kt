package com.waffiq.bazz_movies.core.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.core.network.BuildConfig.TMDB_API_KEY
import com.waffiq.bazz_movies.core.network.BuildConfig.OMDB_API_KEY
import com.waffiq.bazz_movies.core.network.BuildConfig.DEBUG
import com.waffiq.bazz_movies.core.network.BuildConfig.OMDb_API_URL
import com.waffiq.bazz_movies.core.network.BuildConfig.TMDB_API_URL
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.ApiKeyInterceptorOMDb
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.ApiKeyInterceptorTMDB
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.core.network.utils.common.Constants.COUNTRY_API_LINK
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

  private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

  init {
    if (DEBUG) {
      // Enable logging for debug builds
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
      // Disable logging for release builds
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
    }
  }

  @Provides
  fun provideMoshi(): Moshi {
    return Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  @Provides
  fun provideCountryIPApiService(client: OkHttpClient): CountryIPApiService {
    val retrofit = Retrofit.Builder()
      .baseUrl(COUNTRY_API_LINK)
      .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
      .client(client)
      .build()
    return retrofit.create(CountryIPApiService::class.java)
  }

  @Provides
  fun provideOMDBApiService(client: OkHttpClient): OMDbApiService {
    val newClient = client.newBuilder()
      .addInterceptor(ApiKeyInterceptorOMDb(OMDB_API_KEY))
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(OMDb_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
      .client(newClient)
      .build()
    return retrofit.create(OMDbApiService::class.java)
  }

  @Provides
  fun provideTMDBApiService(client: OkHttpClient): TMDBApiService {
    val newClient = client.newBuilder()
      .addInterceptor(ApiKeyInterceptorTMDB(TMDB_API_KEY))
      .build()
    val retrofit = Retrofit.Builder()
      .baseUrl(TMDB_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
      .client(newClient)
      .build()
    return retrofit.create(TMDBApiService::class.java)
  }
}
