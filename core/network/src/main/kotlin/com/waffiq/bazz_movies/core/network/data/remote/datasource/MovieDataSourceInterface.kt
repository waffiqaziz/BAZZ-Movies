package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface MovieDataSourceInterface {
  // PAGING
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItemResponse>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItemResponse>>
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItemResponse>>
  fun getPagingPopularTv(
    region: String,
    twoWeeksFromToday: String
  ): Flow<PagingData<ResultItemResponse>>

  fun getPagingAiringThisWeekTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<ResultItemResponse>>

  fun getPagingAiringTodayTv(
    region: String, airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<ResultItemResponse>>

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
