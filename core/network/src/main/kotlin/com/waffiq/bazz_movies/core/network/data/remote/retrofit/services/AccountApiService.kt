package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApiService {

  @GET("3/account/{account_id}/{category}/{media_type}?sort_by=created_at.asc")
  suspend fun getMediaList(
    @Path("account_id") accountId: Int,
    @Path("category") category: String,
    @Path("media_type") mediaType: String,
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/favorite")
  suspend fun postFavoriteTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: FavoriteRequest,
  ): Response<PostFavoriteWatchlistResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/watchlist")
  suspend fun postWatchlistTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: WatchlistRequest,
  ): Response<PostFavoriteWatchlistResponse>
}
