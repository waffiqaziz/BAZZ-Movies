package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApiService {

  @GET("3/person/{personId}")
  suspend fun getPersonDetails(
    @Path("personId") personId: Int,
    @Query("language") language: String = "en-US",
  ): Response<DetailPersonResponse>

  @GET("3/person/{personId}/images")
  suspend fun getPersonImages(
    @Path("personId") personId: Int,
    @Query("language") language: String = "en-US",
  ): Response<ImagePersonResponse>

  @GET("3/person/{personId}/external_ids")
  suspend fun getPersonExternalIds(
    @Path("personId") personId: Int,
  ): Response<ExternalIDPersonResponse>

  @GET("3/person/{personId}/combined_credits")
  suspend fun getPersonCredits(
    @Path("personId") personId: Int,
    @Query("language") language: String = "en-US",
  ): Response<CombinedCreditResponse>
}
