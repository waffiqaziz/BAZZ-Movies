package com.waffiq.bazz_movies.core.network.data.remote.retrofit.services

import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MovieTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.StatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

  // region AUTHENTICATION
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
  // endregion

  // region TRENDING
  @GET("3/trending/all/week")
  suspend fun getTrendingWeek(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/trending/all/day")
  suspend fun getTrendingDay(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse
  // endregion TRENDING

  // region MOVIE
  @GET("3/movie/popular?language=en-US")
  suspend fun getPopularMovies(
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/upcoming?language=en-US&with_release_type=2|3")
  suspend fun getUpcomingMovies(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/now_playing?language=en-US")
  suspend fun getPlayingNowMovies(
    @Query("region") region: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/movie/top_rated?language=en-US")
  suspend fun getTopRatedMovies(
    @Query("page") page: Int
  ): MovieTvResponse
  // endregion

  // region TV
  @GET(
    "3/discover/tv?language=en-US" +
      "&sort_by=popularity.desc" +
      "&with_runtime.gte=0" +
      "&with_runtime.lte=400" +
      "&with_watch_monetization_types=flatrate|free"
  )
  suspend fun getPopularTv(
    @Query("page") page: Int,
    @Query("watch_region") region: String,
    @Query("air_date.lte") dateTime: String
  ): MovieTvResponse

  @GET(
    "3/discover/tv?language=en-US" +
      "&with_runtime.gte=0" +
      "&with_runtime.lte=400" +
      "&with_watch_monetization_types=flatrate|free" +
      "&with_release_type=2|3"
  )
  suspend fun getTvAiring(
    @Query("watch_region") region: String,
    @Query("air_date.lte") airDateLte: String,
    @Query("air_date.gte") airDateGte: String,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/top_rated?language=en-US")
  suspend fun getTopRatedTv(
    @Query("page") page: Int
  ): MovieTvResponse
  // endregion TV

  // region RECOMMENDATION
  @GET("3/movie/{movieId}/recommendations")
  suspend fun getRecommendedMovie(
    @Path("movieId") movieId: Int,
    @Query("page") page: Int
  ): MovieTvResponse

  @GET("3/tv/{tvId}/recommendations")
  suspend fun getRecommendedTv(
    @Path("tvId") movieId: Int,
    @Query("page") page: Int
  ): MovieTvResponse
  // endregion RECOMMENDATION

  // region FAVORITES & WATCHLIST
  @GET("3/account/{account_id}/favorite/movies?language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteMovies(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/favorite/tv?language=en-US&sort_by=created_at.asc")
  suspend fun getFavoriteTv(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/movies?language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistMovies(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse

  @GET("3/account/{account_id}/watchlist/tv?language=en-US&sort_by=created_at.asc")
  suspend fun getWatchlistTv(
    @Query("session_id") sessionId: String,
    @Query("page") page: Int,
  ): MovieTvResponse
  // endregion

  // region DETAIL PAGE
  @GET("3/movie/{movieId}/account_states")
  suspend fun getStatedMovie(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String
  ): Response<StatedResponse>

  @GET("3/tv/{tvId}/account_states")
  suspend fun getStatedTv(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String
  ): Response<StatedResponse>

  @GET("3/movie/{movieId}/credits?language=en-US")
  suspend fun getCreditMovies(
    @Path("movieId") movieId: Int
  ): Response<MovieTvCreditsResponse>

  @GET("3/tv/{tvId}/credits?language=en-US")
  suspend fun getCreditTv(
    @Path("tvId") tvId: Int
  ): Response<MovieTvCreditsResponse>

  @GET("3/movie/{movieId}?language=en-US&append_to_response=releasedates")
  suspend fun getDetailMovie(
    @Path("movieId") movieId: Int
  ): Response<DetailMovieResponse>

  @GET("3/tv/{tvId}?language=en-US&append_to_response=content_ratings")
  suspend fun getDetailTv(
    @Path("tvId") tvId: Int
  ): Response<DetailTvResponse>

  @GET("3/movie/{id}/videos?language=en-US")
  suspend fun getVideoMovies(
    @Path("id") id: Int
  ): Response<VideoResponse>

  @GET("3/tv/{id}/videos?language=en-US")
  suspend fun getVideoTv(
    @Path("id") id: Int
  ): Response<VideoResponse>

  @GET("3/tv/{tvId}/external_ids?language=en-US")
  suspend fun getExternalId(
    @Path("tvId") tvId: Int
  ): Response<ExternalIdResponse>
  // endregion

  // region PERSON
  @GET("3/person/{personId}?language=en-US")
  suspend fun getDetailPerson(
    @Path("personId") personId: Int
  ): Response<DetailPersonResponse>

  @GET("3/person/{personId}/images?language=en-US")
  suspend fun getImagePerson(
    @Path("personId") personId: Int
  ): Response<ImagePersonResponse>

  @GET("3/person/{personId}/external_ids")
  suspend fun getExternalIdPerson(
    @Path("personId") personId: Int
  ): Response<ExternalIDPersonResponse>

  @GET("3/person/{personId}/combined_credits?language=en-US")
  suspend fun getKnownForPersonCombinedMovieTv(
    @Path("personId") personId: Int
  ): Response<CombinedCreditResponse>
  // endregion

  // region POST FAVORITE & WATCHLIST
  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/favorite")
  suspend fun postFavoriteTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: FavoritePostModel
  ): Response<PostFavoriteWatchlistResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/account/{accountId}/watchlist")
  suspend fun postWatchlistTMDB(
    @Path("accountId") accountId: Int,
    @Query("session_id") sessionId: String,
    @Body data: WatchlistPostModel
  ): Response<PostFavoriteWatchlistResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/movie/{movieId}/rating")
  suspend fun postMovieRate(
    @Path("movieId") movieId: Int,
    @Query("session_id") sessionId: String,
    @Body data: RatePostModel
  ): Response<PostResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @POST("3/tv/{tvId}/rating")
  suspend fun postTvRate(
    @Path("tvId") tvId: Int,
    @Query("session_id") sessionId: String,
    @Body data: RatePostModel
  ): Response<PostResponse>

  @Headers("Content-Type: application/json;charset=utf-8")
  @DELETE("3/authentication/session")
  suspend fun delSession(
    @Query("session_id") sessionId: String
  ): Response<PostResponse>
  // endregion

  @GET("3/search/multi?include_adult=false")
  suspend fun search(
    @Query("query") query: String,
    @Query("page") page: Int,
  ): MultiSearchResponse
}
