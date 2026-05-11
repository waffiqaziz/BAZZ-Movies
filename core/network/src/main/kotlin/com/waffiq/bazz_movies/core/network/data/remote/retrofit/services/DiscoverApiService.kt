package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DiscoverApiService {

  @GET("3/discover/movie")
  suspend fun discoverMovie(@QueryMap params: Map<String, String>): MediaResponse

  @GET("3/discover/tv")
  suspend fun discoverTv(@QueryMap params: Map<String, String>): MediaResponse
}
