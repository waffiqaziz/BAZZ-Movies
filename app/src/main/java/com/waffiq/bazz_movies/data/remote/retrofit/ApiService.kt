package com.waffiq.bazz_movies.data.remote.retrofit


import com.waffiq.bazz_movies.BuildConfig.API_KEY
import com.waffiq.bazz_movies.data.remote.response.*
import retrofit2.Call

import retrofit2.http.*

interface ApiService {
  @POST("3/authentication/token/new?api_key=$API_KEY")
  suspend fun createToken(): CreateTokenResponse

  @POST("login")
  suspend fun login(
    @Field("email") email: String,
    @Field("password") pass: String
  ): ApiResponse


  @GET("3/movie/top_rated?api_key=$API_KEY")
  suspend fun getTopRatedMovies(
    @Query("language") language: String,
    @Query("page") page : Int
  ): MoviesResponse

  @GET("3/trending/all/week?api_key=$API_KEY")
  suspend fun getTrending(
    @Query("page") page : Int
  ): TrendingAllResponse

  @GET("3/movie/now_playing?api_key=$API_KEY&language=en-US&page=1")
  fun getMovieNowPlaying(): Call<MoviesResponse>

  @GET("3/movie/upcoming?api_key=$API_KEY&language=en-US&region=US&with_release_type=2|3")
  suspend fun getUpcomingMovies(
    @Query("page") page : Int
  ): MoviesResponse

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
  fun getCredits(
    @Path("movie_id") movie_id: Int
  ): Call<CreditsResponse>
}
