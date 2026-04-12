package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface MovieRemoteDataSourceInterface {
  // PAGING
  fun getTopRatedMovies(): Flow<PagingData<MediaResponseItem>>
  fun getPopularMovies(): Flow<PagingData<MediaResponseItem>>
  fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaResponseItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaResponseItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaResponseItem>>

  // DETAIL PAGE
  fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>>
  fun getMovieState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>>
  fun getMovieWatchProviders(id: Int): Flow<NetworkResult<WatchProvidersResponse>>
  fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  fun getMovieKeywords(movieId: String): Flow<NetworkResult<MovieKeywordsResponse>>

  // POST
  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<NetworkResult<PostResponse>>
}
