package com.waffiq.bazz_movies.data.remote.retrofit

import com.waffiq.bazz_movies.BuildConfig.API_KEY
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.tmdb.*
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

  @GET("3/movie/{movieId}/recommendations?api_key=$API_KEY")
  suspend fun getRecommendedMovie(
    @Path("movieId") movieId: Int,
    @Query("page") page : Int
  ): MovieTvResponse

  @GET("3/tv/{tvId}/recommendations?api_key=$API_KEY")
  suspend fun getRecommendedTv(
    @Path("tvId") movieId: Int,
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
    @Query("session_id") sessionId : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteTv(
    @Query("session_id") sessionId : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/movies?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistMovies(
    @Query("session_id") sessionId : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/tv?api_key=$API_KEY&language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistTv(
    @Query("session_id") sessionId : String,
    @Query("page") page : Int,
  ): MovieTvResponse

  @GET("3/movie/{movieId}/account_states?api_key=$API_KEY")
  fun getStatedMovie(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId : String
  ): Call<StatedResponse>

  @GET("3/tv/{tvId}/account_states?api_key=$API_KEY")
  fun getStatedTv(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId : String
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
  fun getCreditMovies(
    @Path("movieId") movieId: Int
  ): Call<CreditsResponse>

  @GET("3/tv/{tvId}/credits?api_key=$API_KEY&language=en-US")
  fun getCreditTv(
    @Path("tvId") tvId: Int
  ): Call<CreditsResponse>

  @GET("3/movie/{movieId}?api_key=$API_KEY&language=en-US&append_to_response=release_dates")
  fun getDetailMovie(
    @Path("movieId") movieId: Int
  ): Call<DetailMovieResponse>

  @GET("3/tv/{tvId}?api_key=$API_KEY&language=en-US&append_to_response=content_ratings")
  fun getDetailTv(
    @Path("tvId") tvId: Int
  ): Call<DetailTvResponse>

  @GET("3/movie/{id}/videos?api_key=$API_KEY&language=en-US")
  fun getVideoMovies(
    @Path("id") id: Int
  ): Call<VideoResponse>

  @GET("3/tv/{id}/videos?api_key=$API_KEY&language=en-US")
  fun getVideoTv(
    @Path("id") id: Int
  ): Call<VideoResponse>

  @GET("3/tv/{tvId}/external_ids?api_key=$API_KEY&language=en-US")
  fun getExternalId(
    @Path("tvId") tvId: Int
  ): Call<ExternalIdResponse>

  @GET("3/person/{personId}?api_key=$API_KEY&language=en-US")
  fun getDetailPerson(
    @Path("personId") personId: Int
  ): Call<DetailPersonResponse>

  @GET("3/person/{personId}/movie_credits?api_key=$API_KEY&language=en-US")
  fun getKnownForPerson(
    @Path("personId") personId: Int
  ): Call<CreditsPersonResponse>

  @GET("3/person/{personId}/images?api_key=$API_KEY&language=en-US")
  fun getImagePerson(
    @Path("personId") personId: Int
  ): Call<ImagePersonResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/favorite?api_key=$API_KEY")
  fun postFavoriteTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId : String,
    @Body data: Favorite
  ): Call<PostFavoriteWatchlistResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/watchlist?api_key=$API_KEY")
  fun postWatchlistTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId : String,
    @Body data: Watchlist
  ): Call<PostFavoriteWatchlistResponse>

}
