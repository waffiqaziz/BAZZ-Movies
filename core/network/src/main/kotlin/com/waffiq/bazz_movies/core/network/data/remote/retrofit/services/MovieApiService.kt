package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
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

interface MovieApiService {

  @GET("3/movie/popular")
  suspend fun getPopularMovies(
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/movie/upcoming?with_release_type=2|3")
  suspend fun getUpcomingMovies(
    @Query("region") region: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/movie/now_playing")
  suspend fun getNowPlayingMovies(
    @Query("region") region: String,
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/movie/top_rated")
  suspend fun getTopRatedMovies(
    @Query("page") page: Int,
    @Query("language") language: String = "en-US",
  ): MediaResponse

  @GET("3/movie/{movieId}/account_states")
  suspend fun getMovieState(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String,
  ): Response<MediaStateResponse>

  @GET("3/movie/{movieId}/credits")
  suspend fun getMovieCredits(
    @Path("movieId") movieId: Int,
    @Query("language") language: String = "en-US",
  ): Response<MediaCreditsResponse>

  @GET("3/movie/{movieId}?append_to_response=release_dates")
  suspend fun getMovieDetail(
    @Path("movieId") movieId: Int,
    @Query("language") language: String = "en-US",
  ): Response<DetailMovieResponse>

  @GET("3/movie/{id}/videos")
  suspend fun getMovieVideo(
    @Path("id") id: Int,
    @Query("language") language: String = "en-US",
  ): Response<VideoResponse>

  @GET("3/movie/{movieId}/recommendations")
  suspend fun getMovieRecommendations(
    @Path("movieId") movieId: Int,
    @Query("page") page: Int,
  ): MediaResponse

  @GET("3/movie/{movieId}/keywords")
  suspend fun getMovieKeywords(@Path("movieId") movieId: String): Response<MovieKeywordsResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/movie/{movieId}/rating")
  suspend fun postMovieRate(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String,
    @Body data: RatingRequest,
  ): Response<PostResponse>

  @GET("3/movie/{id}/watch/providers")
  suspend fun getMovieWatchProviders(@Path("id") id: Int): Response<WatchProvidersResponse>
}
