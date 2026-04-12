package com.waffiq.bazz_movies.core.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waffiq.bazz_movies.core.network.BuildConfig.OMDB_API_KEY
import com.waffiq.bazz_movies.core.network.BuildConfig.OMDb_API_URL
import com.waffiq.bazz_movies.core.network.BuildConfig.TMDB_API_KEY
import com.waffiq.bazz_movies.core.network.BuildConfig.TMDB_API_URL
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter.RatedResponseAdapter
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors.ApiKeyInterceptorOMDb
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.interceptors.ApiKeyInterceptorTMDB
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AccountApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AuthApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.DiscoverApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.MovieApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.PersonApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.SearchApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TrendingApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TvApiService
import com.waffiq.bazz_movies.core.network.domain.IDebugConfig
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
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

  @Singleton
  @Provides
  fun provideLoggingInterceptor(debugConfig: IDebugConfig): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
      level = if (debugConfig.isDebug()) {
        HttpLoggingInterceptor.Level.BODY
      } else {
        HttpLoggingInterceptor.Level.NONE
      }
    }

  @Singleton
  @Provides
  fun provideMoshi(): Moshi =
    Moshi.Builder()
      .add(RatedResponseAdapter())
      .add(KotlinJsonAdapterFactory())
      .build()

  @Singleton
  @Provides
  fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
      .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
      .readTimeout(TIME_OUT, TimeUnit.SECONDS)
      .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
      .addInterceptor(loggingInterceptor)
      .build()

  @Provides
  @Singleton
  @Named("COUNTRY")
  fun provideCountryRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
      .baseUrl(COUNTRY_API_LINK)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()

  @Provides
  fun provideCountryApi(@Named("COUNTRY") retrofit: Retrofit): CountryIPApiService =
    retrofit.create()

  @Provides
  @Singleton
  @Named("OMDB")
  fun provideOMDbRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    val newClient = client.newBuilder()
      .addInterceptor(ApiKeyInterceptorOMDb(OMDB_API_KEY))
      .build()

    return Retrofit.Builder()
      .baseUrl(OMDb_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(newClient)
      .build()
  }

  @Provides
  fun provideOMDbApi(@Named("OMDB") retrofit: Retrofit): OMDbApiService = retrofit.create()

  @Provides
  @Singleton
  @TMDB
  fun provideTMDBRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    val newClient = client.newBuilder()
      .addInterceptor(ApiKeyInterceptorTMDB(TMDB_API_KEY))
      .build()

    return Retrofit.Builder()
      .baseUrl(TMDB_API_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(newClient)
      .build()
  }

  @Provides
  fun provideAccountApi(@TMDB retrofit: Retrofit): AccountApiService = retrofit.create()

  @Provides
  fun provideAuthApi(@TMDB retrofit: Retrofit): AuthApiService = retrofit.create()

  @Provides
  fun provideDiscoverApi(@TMDB retrofit: Retrofit): DiscoverApiService = retrofit.create()

  @Provides
  fun provideMovieApi(@TMDB retrofit: Retrofit) = retrofit.create<MovieApiService>()

  @Provides
  fun providePersonApi(@TMDB retrofit: Retrofit): PersonApiService = retrofit.create()

  @Provides
  fun provideSearchApi(@TMDB retrofit: Retrofit): SearchApiService = retrofit.create()

  @Provides
  fun provideTrendingApi(@TMDB retrofit: Retrofit): TrendingApiService = retrofit.create()

  @Provides
  fun provideTvApi(@TMDB retrofit: Retrofit): TvApiService = retrofit.create()

  @Qualifier
  @Retention(AnnotationRetention.BINARY)
  annotation class TMDB

  private inline fun <reified T> Retrofit.create(): T = create(T::class.java)

  companion object {
    private const val TIME_OUT = 30L
  }
}
