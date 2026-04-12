package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApiService {

  @GET("3/discover/movie?include_adult=false&sort_by=popularity.desc")
  suspend fun getMovieByGenres(
    @Query("with_genres") genres: String,
    @Query("watch_region") region: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/discover/tv?include_adult=false&sort_by=popularity.desc")
  suspend fun getTvByGenres(
    @Query("with_genres") genres: String,
    @Query("watch_region") region: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/discover/movie?include_adult=false&sort_by=popularity.desc")
  suspend fun getMovieByKeywords(
    @Query("with_keywords") genres: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/discover/tv?include_adult=false&sort_by=popularity.desc")
  suspend fun getTvByKeywords(
    @Query("with_keywords") genres: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse
}
