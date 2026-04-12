package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApiService {

  @GET(
    "3/discover/tv?" +
      "sort_by=popularity.desc" +
      "&with_runtime.gte=0" +
      "&with_runtime.lte=400" +
      "&with_watch_monetization_types=flatrate|free",
  )
  suspend fun getPopularTv(
    @Query("watch_region") region: String,
    @Query("air_date.lte") dateTime: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET(
    "3/discover/tv?" +
      "with_runtime.gte=0" +
      "&with_runtime.lte=400" +
      "&with_watch_monetization_types=flatrate|free" +
      "&with_release_type=2|3",
  )
  suspend fun getAiringTv(
    @Query("watch_region") region: String,
    @Query("air_date.lte") airDateLte: String,
    @Query("air_date.gte") airDateGte: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/tv/top_rated")
  suspend fun getTopRatedTv(
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/tv/{tvId}/recommendations")
  suspend fun getTvRecommendations(
    @Path("tvId") movieId: Int,
    @Query("page") page: Int,
  ): MediaResponse

  @GET("3/tv/{tvId}/account_states")
  suspend fun getTvState(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String,
  ): Response<MediaStateResponse>

  @GET("3/tv/{id}/videos")
  suspend fun getTvVideo(
    @Path("id") id: Int,
    @Query("language") language: String = "en-US",
  ): Response<VideoResponse>

  @GET("3/tv/{tvId}/external_ids")
  suspend fun getTvExternalIds(
    @Path("tvId") tvId: Int,
    @Query("language") language: String = "en-US",
  ): Response<ExternalIdResponse>

  @GET("3/tv/{tvId}?append_to_response=content_ratings")
  suspend fun getTvDetail(
    @Path("tvId") tvId: Int,
    @Query("language") language: String = "en-US",
  ): Response<DetailTvResponse>

  @GET("3/tv/{tvId}/credits")
  suspend fun getTvCredits(
    @Path("tvId") tvId: Int,
    @Query("language") language: String = "en-US",
  ): Response<MediaCreditsResponse>

  @GET("3/tv/{tvId}/keywords")
  suspend fun getTvKeywords(@Path("tvId") tvId: String): Response<TvKeywordsResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/tv/{tvId}/rating")
  suspend fun postTvRate(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String,
    @Body data: RatingRequest,
  ): Response<PostResponse>

  @GET("3/tv/{id}/watch/providers")
  suspend fun getTvWatchProviders(@Path("id") id: Int): Response<WatchProvidersResponse>
}
