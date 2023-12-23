package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.data.remote.response.*
import retrofit2.Call

import retrofit2.http.*

interface IMDBApiLibService {

  @GET("en/API/Ratings/{apiKey}/{imdb_id}")
  fun getScore(
    @Path("apiKey") apiKey: String,
    @Path("imdbId") imdbId: String
  ): Call<ScoreRatingResponse>

}
