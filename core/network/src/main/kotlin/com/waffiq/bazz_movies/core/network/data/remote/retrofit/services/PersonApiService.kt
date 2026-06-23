package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApiService {

  @GET("3/person/{personId}?append_to_response=combined_credits")
  suspend fun getPersonDetails(
    @Path("personId") personId: Int,
    @Query("language") language: String = "en-US",
  ): Response<DetailPersonResponse>

  @GET("3/person/{personId}/images")
  suspend fun getPersonImages(
    @Path("personId") personId: Int,
    @Query("language") language: String = "en-US",
  ): Response<ImagePersonResponse>
}
