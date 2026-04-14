package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingApiService {

  @GET("3/trending/all/week")
  suspend fun getTrendingThisWeek(
    @Query("region") region: String,
    @Query("page") page: Int,
  ): MediaResponse

  @GET("3/trending/all/day")
  suspend fun getTrendingToday(
    @Query("region") region: String,
    @Query("page") page: Int,
  ): MediaResponse
}
