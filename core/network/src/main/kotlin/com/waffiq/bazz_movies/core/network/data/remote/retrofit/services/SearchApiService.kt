package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

fun interface SearchApiService {

  @GET("3/search/multi?include_adult=false")
  suspend fun search(@Query("query") query: String, @Query("page") page: Int): MultiSearchResponse
}
