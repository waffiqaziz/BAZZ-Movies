package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.API_KEY_OMDb
import com.waffiq.bazz_movies.data.remote.response.omdb.*
import retrofit2.Call

import retrofit2.http.*

interface OMDbApiService {

  @GET("?apikey=$API_KEY_OMDb&r=json")
  fun getMovieDetailOMDb(
    @Query("i") i: String
  ): Call<OMDbDetailsResponse>

}
