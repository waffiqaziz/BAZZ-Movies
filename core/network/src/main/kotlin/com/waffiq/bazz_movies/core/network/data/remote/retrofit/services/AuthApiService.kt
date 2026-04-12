package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

  @GET("3/authentication/token/new")
  suspend fun createToken(): Response<AuthenticationResponse>

  @POST("3/authentication/token/validate_with_login")
  suspend fun login(
    @Query("username") username: String,
    @Query("password") pass: String,
    @Query("request_token") requestToken: String,
  ): Response<AuthenticationResponse>

  @GET("3/authentication/session/new")
  suspend fun createSessionLogin(
    @Query("request_token") requestToken: String,
  ): Response<CreateSessionResponse>

  @GET("3/account")
  suspend fun getAccountDetails(
    @Query("session_id") sessionId: String,
  ): Response<AccountDetailsResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @DELETE("3/authentication/session")
  suspend fun deleteSession(@Query("session_id") sessionId: String): Response<PostResponse>
}
