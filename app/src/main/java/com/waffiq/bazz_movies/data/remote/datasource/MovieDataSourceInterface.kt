package com.waffiq.bazz_movies.data.remote.datasource

import android.util.Log
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieDataSourceInterface {

  // CALL FUNCTION
  suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    try {
      val response = apiCall.invoke()
      if (response != null && response.isSuccessful) return NetworkResult.success(response.body())

      val errorBody = response?.errorBody()?.string()
      return if (response?.code() == 404) NetworkResult.error("Bad Request")
      else if (!errorBody.isNullOrEmpty()) NetworkResult.error(errorBody)
      else NetworkResult.error("Error in fetching data")
    } catch (e: Exception) {
      println(e.message)
      throw RuntimeException(e)
    }
  }

  // PAGING
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>>
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingPopularTv(): Flow<PagingData<ResultItem>>
  fun getPagingOnTv(): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>>

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
}