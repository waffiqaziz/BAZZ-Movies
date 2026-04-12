package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AccountRemoteDataSourceInterface {
  fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaResponseItem>>
  fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaResponseItem>>

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
}
