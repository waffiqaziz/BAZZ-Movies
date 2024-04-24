package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApiService {

  @GET("?r=json")
  suspend fun getMovieDetailOMDb(
    @Query("i") i: String
  ): Response<OMDbDetailsResponse>
}
