package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.API_KEY_OMDb
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApiService {

  @GET("?apikey=$API_KEY_OMDb&r=json")
  suspend fun getMovieDetailOMDb(
    @Query("i") i: String
  ): Response<OMDbDetailsResponse>
}
