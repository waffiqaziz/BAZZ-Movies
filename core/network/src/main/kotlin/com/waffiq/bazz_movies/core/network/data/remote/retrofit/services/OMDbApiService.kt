package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

fun interface OMDbApiService {

  @GET("?r=json")
  suspend fun getOMDbDetails(
    @Query("i") i: String
  ): Response<OMDbDetailsResponse>
}
