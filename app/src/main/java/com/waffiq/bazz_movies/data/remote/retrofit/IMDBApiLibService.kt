package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.*
import retrofit2.Call

import retrofit2.http.*

interface IMDBApiLibService {

  @GET("en/API/Ratings/{api_key}/{imdb_id}")
  fun getScore(
    @Path("api_key") api_key: String,
    @Path("imdb_id") imdb_id: String
  ): Call<ScoreRatingResponse>

}
