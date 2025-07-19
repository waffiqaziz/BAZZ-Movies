package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface MovieDataSourceInterface {
  // PAGING
  fun getTopRatedMovies(): Flow<PagingData<MediaResponseItem>>
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaResponseItem>>
  fun getTrendingToday(region: String): Flow<PagingData<MediaResponseItem>>
  fun getPopularMovies(): Flow<PagingData<MediaResponseItem>>
  fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getPopularTv(region: String, twoWeeksFromToday: String): Flow<PagingData<MediaResponseItem>>

  fun getAiringThisWeekTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>>

  fun getAiringTodayTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>>

  fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaResponseItem>>
  fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaResponseItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaResponseItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaResponseItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaResponseItem>>
  fun search(query: String): Flow<PagingData<MultiSearchResponseItem>>

  // DETAIL PAGE
  suspend fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>>
  suspend fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  suspend fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  suspend fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>>
  suspend fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>>
  suspend fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>>
  suspend fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>>
  suspend fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>>
  suspend fun getMovieState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  suspend fun getTvState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  suspend fun getWatchProviders(
    params: String,
    id: Int,
  ): Flow<NetworkResult<WatchProvidersResponse>>

  // PERSON
  suspend fun getPersonDetail(id: Int): Flow<NetworkResult<DetailPersonResponse>>
  suspend fun getPersonImage(id: Int): Flow<NetworkResult<ImagePersonResponse>>
  suspend fun getPersonKnownFor(id: Int): Flow<NetworkResult<CombinedCreditResponse>>
  suspend fun getPersonExternalID(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>>

  // POST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<NetworkResult<PostResponse>>

  suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<NetworkResult<PostResponse>>
}
