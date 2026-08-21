package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

  @GET("3/search/multi?include_adult=false")
  suspend fun searchMulti(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse

  @GET("3/search/movie?include_adult=false")
  suspend fun searchMovies(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse

  @GET("3/search/tv?include_adult=false")
  suspend fun searchTv(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse

  @GET("3/search/person?include_adult=false")
  suspend fun searchPerson(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse
}
