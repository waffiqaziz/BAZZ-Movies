package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
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

  fun getAiringTv(
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
  fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>>
  fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>>
  fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>>
  fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>>
  fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>>
  fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>>
  fun getMovieState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  fun getTvState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  fun getWatchProviders(
    params: String,
    id: Int,
  ): Flow<NetworkResult<WatchProvidersResponse>>
  fun getMovieKeywords(movieId: String): Flow<NetworkResult<MovieKeywordsResponse>>
  fun getTvKeywords(tvId: String): Flow<NetworkResult<TvKeywordsResponse>>

  // PERSON
  fun getPersonDetails(id: Int): Flow<NetworkResult<DetailPersonResponse>>
  fun getPersonImages(id: Int): Flow<NetworkResult<ImagePersonResponse>>
  fun getPersonCredits(id: Int): Flow<NetworkResult<CombinedCreditResponse>>
  fun getPersonExternalIds(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>>

  // POST
  fun postFavorite(
    sessionId: String,
    fav: FavoriteRequest,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  fun postWatchlist(
    sessionId: String,
    wtc: WatchlistRequest,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<NetworkResult<PostResponse>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<NetworkResult<PostResponse>>
}
