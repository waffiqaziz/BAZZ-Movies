package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface MovieDataSourceInterface {

  // CALL FUNCTION
  suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    try {
      val response = apiCall()
      if (response != null && response.isSuccessful) return NetworkResult.success(response.body())

      val errorBody = response?.errorBody()?.string()
      return if (response?.code() == 404) {
        NetworkResult.error("Bad Request")
      } else if (!errorBody.isNullOrEmpty()) {
        NetworkResult.error(errorBody)
      } else {
        NetworkResult.error("Error in fetching data")
      }
    } catch (e: HttpException) {
      return NetworkResult.error(e.message ?: "Something went wrong")
    } catch (e: SocketTimeoutException) {
      return NetworkResult.error("Connection timed out. Please try again.")
    } catch (e: UnknownHostException) {
      return NetworkResult.error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: IOException) {
      return NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      return NetworkResult.error(e.toString())
    }
  }

  suspend fun <T> safeApiCallPost(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    return try {
      val response = apiCall()
      if (response != null && response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.success(responseBody)
        } else {
          NetworkResult.error("Error in fetching data")
        }
      } else {
        val errorMessage = response?.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: HttpException) {
      return NetworkResult.error(e.message ?: "Something went wrong")
    } catch (e: SocketTimeoutException) {
      return NetworkResult.error("Connection timed out. Please try again.")
    } catch (e: UnknownHostException) {
      return NetworkResult.error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error(e.toString())
    }
  }

  // PAGING
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItemResponse>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItemResponse>>
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingPopularTv(twoWeeksFromToday: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingOnTv(): Flow<PagingData<ResultItemResponse>>
  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItemResponse>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItemResponse>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItemResponse>>
  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItemResponse>>
  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearchResponse>>

  // DETAIL PAGE
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>>
  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCreditsResponse>>
  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCreditsResponse>>
  suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<VideoResponse>>
  suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<VideoResponse>>
  suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovieResponse>>
  suspend fun getDetailTv(id: Int): Flow<NetworkResult<DetailTvResponse>>
  suspend fun getExternalTvId(id: Int): Flow<NetworkResult<ExternalIdResponse>>
  suspend fun getStatedMovie(sessionId: String, id: Int): Flow<NetworkResult<StatedResponse>>
  suspend fun getStatedTv(sessionId: String, id: Int): Flow<NetworkResult<StatedResponse>>

  // PERSON
  suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPersonResponse>>
  suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePersonResponse>>
  suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditResponse>>
  suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>>

  // POST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<PostResponse>>

  suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<PostResponse>>
}
