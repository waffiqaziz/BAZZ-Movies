package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApiService {

  @GET("3/discover/movie")
  suspend fun discoverMovie(
    @Query("include_adult") includeAdult: Boolean = false,
    @Query("language") language: String = "en-US",
    @Query("page") page: Int,
    @Query("release_date.gte") releaseDateGte: String? = null,
    @Query("release_date.lte") releaseDateLte: String? = null,
    @Query("sort_by") sortBy: String = "popularity.desc",
    @Query("with_genres") genres: String? = null,
    @Query("with_keywords") keywords: String? = null,
    @Query("watch_region") region: String? = null,
  ): MediaResponse

  @GET("3/discover/tv")
  suspend fun discoverTv(
    @Query("first_air_date.gte") firstAirDateGte: String? = null,
    @Query("first_air_date.lte") firstAirDateLte: String? = null,
    @Query("include_adult") includeAdult: Boolean = false,
    @Query("language") language: String = "en-US",
    @Query("page") page: Int,
    @Query("sort_by") sortBy: String = "popularity.desc",
    @Query("with_genres") genres: String? = null,
    @Query("with_keywords") keywords: String? = null,
    @Query("with_origin_country") country: String? = null,
    @Query("watch_region") region: String? = null,
    @Query("without_genres") withoutGenres: String? = null,
    @Query("without_keywords") withoutKeywords: String? = null,
  ): MediaResponse
}
