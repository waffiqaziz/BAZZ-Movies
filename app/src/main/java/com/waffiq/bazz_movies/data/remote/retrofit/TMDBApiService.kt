package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.API_KEY
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.tmdb.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MultiSearchResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {
  @GET("3/authentication/token/new?api_key=$API_KEY")
  fun createToken(): Call<AuthenticationResponse>

  @POST("3/authentication/token/validate_with_login?api_key=$API_KEY")
  fun login(
    @Query("username") username: String,
    @Query("password") pass: String,
    @Query("request_token") requestToken: String,
  ): Call<AuthenticationResponse>

  @GET("3/authentication/session/new?api_key=$API_KEY")
  fun createSessionLogin(
    @Query("request_token") requestToken: String,
  ): Call<CreateSessionResponse>

  @GET("3/account?api_key=$API_KEY")
  fun getAccountDetails(
    @Query("session_id") sessionId: String,
  ): Call<AccountDetailsResponse>

  @GET("3/movie/top_rated?api_key=$API_KEY&language=en-US")
  suspend fun getTopRatedMovies(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/top_rated?api_key=$API_KEY&language=en-US")
  suspend fun getTopRatedTv(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/trending/all/week?api_key=$API_KEY")
  suspend fun getTrendingWeek(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/trending/all/day?api_key=$API_KEY")
  suspend fun getTrendingDay(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/{movieId}/recommendations?api_key=$API_KEY")
  suspend fun getRecommendedMovie(
    @Path("movieId") movieId: Int,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/{tvId}/recommendations?api_key=$API_KEY")
  suspend fun getRecommendedTv(
    @Path("tvId") movieId: Int,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/on_the_air?api_key=$API_KEY&language=en-US")
  suspend fun getTvOnTheAir(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/airing_today?api_key=$API_KEY&language=en-US")
  suspend fun getTvAiringToday(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/upcoming?api_key=$API_KEY&language=en-US&with_release_type=2|3")
  suspend fun getUpcomingMovies(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/now_playing?api_key=$API_KEY&language=en-US")
  suspend fun getPlayingNowMovies(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/popular?api_key=$API_KEY&language=en-US")
  suspend fun getPopularMovies(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/popular?api_key=$API_KEY&language=en-US&sort_by=popularity.desc&watch_region=CA&with_runtime.gte=0&with_runtime.lte=400&with_watch_monetization_types=flatrate|free|ads|rent|buy")
  suspend fun getPopularTv(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/movies?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteMovies(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteTv(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/movies?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistMovies(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistTv(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/movie/{movieId}/account_states?api_key=$API_KEY")
  fun getStatedMovie(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String
  ): Call<StatedResponse>

  @GET("3/tv/{tvId}/account_states?api_key=$API_KEY")
  fun getStatedTv(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String
  ): Call<StatedResponse>

//  @GET("3/genre/movie/list?api_key=$API_KEY")
//  fun getMovieGenres(
//    @Query("language") language: String = "en"
//  ): Call<GenresResponse>

  @GET("3/search/multi?api_key=$API_KEY&include_adult=false")
  suspend fun search(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse

  @GET("3/movie/{movieId}/credits?api_key=$API_KEY&language=en-US")
  suspend fun getCreditMovies(
    @Path("movieId") movieId: Int
  ): Response<MovieTvCreditsResponse>

  @GET("3/tv/{tvId}/credits?api_key=$API_KEY&language=en-US")
  suspend fun getCreditTv(
    @Path("tvId") tvId: Int
  ): Response<MovieTvCreditsResponse>

  @GET("3/movie/{movieId}?api_key=$API_KEY&language=en-US&append_to_response=release_dates")
  suspend fun getDetailMovie(
    @Path("movieId") movieId: Int
  ): Response<DetailMovieResponse>

  @GET("3/tv/{tvId}?api_key=$API_KEY&language=en-US&append_to_response=content_ratings")
  suspend fun getDetailTv(
    @Path("tvId") tvId: Int
  ): Response<DetailTvResponse>

  @GET("3/movie/{id}/videos?api_key=$API_KEY&language=en-US")
  suspend fun getVideoMovies(
    @Path("id") id: Int
  ): Response<VideoResponse>

  @GET("3/tv/{id}/videos?api_key=$API_KEY&language=en-US")
  suspend fun getVideoTv(
    @Path("id") id: Int
  ): Response<VideoResponse>

  @GET("3/tv/{tvId}/external_ids?api_key=$API_KEY&language=en-US")
  suspend fun getExternalId(
    @Path("tvId") tvId: Int
  ): Response<ExternalIdResponse>

  @GET("3/person/{personId}?api_key=$API_KEY&language=en-US")
  fun getDetailPerson(
    @Path("personId") personId: Int
  ): Call<DetailPersonResponse>

  @GET("3/person/{personId}/images?api_key=$API_KEY&language=en-US")
  fun getImagePerson(
    @Path("personId") personId: Int
  ): Call<ImagePersonResponse>

  @GET("3/person/{personId}/external_ids?api_key=$API_KEY")
  fun getExternalIdPerson(
    @Path("personId") personId: Int
  ): Call<ExternalIDPersonResponse>

  @GET("3/person/{personId}/combined_credits?api_key=$API_KEY&language=en-US")
  fun getKnownForPersonCombinedMovieTv(
    @Path("personId") personId: Int
  ): Call<CombinedCreditResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/favorite?api_key=$API_KEY")
  fun postFavoriteTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: Favorite
  ): Call<PostResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/watchlist?api_key=$API_KEY")
  fun postWatchlistTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: Watchlist
  ): Call<PostResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/movie/{movieId}/rating?api_key=$API_KEY")
  fun postMovieRate(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String,
    @Body data: Rate
  ): Call<PostRateResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/tv/{tvId}/rating?api_key=$API_KEY")
  fun postTvRate(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String,
    @Body data: Rate
  ): Call<PostRateResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @DELETE("3/authentication/session?api_key=$API_KEY")
  fun delSession(
    @Query("session_id") sessionId: String
  ): Call<PostRateResponse>
}
