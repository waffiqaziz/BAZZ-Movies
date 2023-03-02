package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.API_KEY
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.*
import retrofit2.Call

import retrofit2.http.*

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
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/tv/top_rated?api_key=$API_KEY&language=en-US")
  suspend fun getTopRatedTv(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/trending/all/week?api_key=$API_KEY")
  suspend fun getTrending(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/tv/on_the_air?api_key=$API_KEY&language=en-US&page=1")
  suspend fun getTvOnTheAir(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/tv/airing_today?api_key=$API_KEY&language=en-US&page=1")
  suspend fun getTvAiringToday(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/movie/upcoming?api_key=$API_KEY&language=en-US&region=US&with_release_type=2|3")
  suspend fun getUpcomingMovies(
    @Query("page") page : Int
  ): MovieTvResponse


  @GET("3/movie/now_playing?api_key=$API_KEY&language=en-US")
  suspend fun getPlayingNowMovies(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/movie/popular?api_key=$API_KEY&language=en-US")
  suspend fun getPopularMovies(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/tv/popular?api_key=$API_KEY&language=en-US")
  suspend fun getPopularTv(
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/movies?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteMovies(
    @Query("session_id") session_id : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteTv(
    @Query("session_id") session_id : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/movies?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistMovies(
    @Query("session_id") session_id : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistTv(
    @Query("session_id") session_id : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/movie/{movie_id}/account_states?api_key=$API_KEY")
  fun getStatedMovie(
    @Path("movie_id") movie_id: Int,
    @Query("session_id") session_id : String
  ): Call<StatedResponse>

  @GET("3/tv/{tv_id}/account_states?api_key=$API_KEY")
  fun getStatedTv(
    @Path("tv_id") movie_id: Int,
    @Query("session_id") session_id : String
  ): Call<StatedResponse>

//  @GET("3/genre/movie/list?api_key=$API_KEY")
//  fun getMovieGenres(
//    @Query("language") language: String = "en"
//  ): Call<GenresResponse>

  @GET("3/search/multi?api_key=$API_KEY")
  suspend fun search(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse

  @GET("3/movie/{movie_id}/credits?api_key=$API_KEY&language=en-US")
  fun getCreditMovies(
    @Path("movie_id") movie_id: Int
  ): Call<CreditsResponse>

  @GET("3/tv/{tv_id}/credits?api_key=$API_KEY&language=en-US")
  fun getCreditTv(
    @Path("tv_id") tv_id: Int
  ): Call<CreditsResponse>

  @GET("3/movie/{movie_id}?api_key=$API_KEY&language=en-US")
  fun getDetailMovie(
    @Path("movie_id") movie_id: Int
  ): Call<DetailMovieResponse>

  @GET("3/tv/{tv_id}?api_key=$API_KEY&language=en-US")
  fun getDetailTv(
    @Path("tv_id") tv_id: Int
  ): Call<DetailTvResponse>

  @GET("3/tv/{tv_id}/external_ids?api_key=$API_KEY&language=en-US")
  fun getExternalId(
    @Path("tv_id") tv_id: Int
  ): Call<ExternalIdResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{account_id}/favorite?api_key=$API_KEY")
  fun postFavoriteTMDB(
    @Path("account_id") accountId: Int,
    @Query("session_id") session_id : String,
    @Body data: Favorite
  ): Call<PostFavoriteWatchlistResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{account_id}/watchlist?api_key=$API_KEY")
  fun postWatchlistTMDB(
    @Path("account_id") accountId: Int,
    @Query("session_id") session_id : String,
    @Body data: Watchlist
  ): Call<PostFavoriteWatchlistResponse>

}
