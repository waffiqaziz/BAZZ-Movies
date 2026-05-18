package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.constants.AccountMediaCategory
import com.waffiq.bazz_movies.core.network.data.remote.constants.MediaType
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AccountApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.PageHelper.createPager
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountRemoteDataSource @Inject constructor(
  private val accountApiService: AccountApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AccountRemoteDataSourceInterface {

  override fun getFavoriteMovies(userId: Int, sessionId: String) =
    getAccountMediaPaging(
      userId = userId,
      sessionId = sessionId,
      category = AccountMediaCategory.FAVORITE,
      mediaType = MediaType.MOVIES,
    )

  override fun getFavoriteTv(userId: Int, sessionId: String) =
    getAccountMediaPaging(
      userId = userId,
      sessionId = sessionId,
      category = AccountMediaCategory.FAVORITE,
      mediaType = MediaType.TV,
    )

  override fun getWatchlistTv(userId: Int, sessionId: String) =
    getAccountMediaPaging(
      userId = userId,
      sessionId = sessionId,
      category = AccountMediaCategory.WATCHLIST,
      mediaType = MediaType.TV,
    )

  override fun getWatchlistMovies(userId: Int, sessionId: String) =
    getAccountMediaPaging(
      userId = userId,
      sessionId = sessionId,
      category = AccountMediaCategory.WATCHLIST,
      mediaType = MediaType.MOVIES,
    )

  override fun postFavorite(
    sessionId: String,
    fav: FavoriteRequest,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    executeApiCall(
      apiCall = { accountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      ioDispatcher = ioDispatcher,
    )

  override fun postWatchlist(
    sessionId: String,
    wtc: WatchlistRequest,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    executeApiCall(
      apiCall = { accountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      ioDispatcher = ioDispatcher,
    )

  private fun getAccountMediaPaging(
    userId: Int,
    sessionId: String,
    category: AccountMediaCategory,
    mediaType: MediaType,
  ): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      accountApiService.getMediaList(
        accountId = userId,
        sessionId = sessionId,
        category = category.asApiValue(),
        mediaType = mediaType.asApiValue(),
        page = page,
      ).results
    }.flow.flowOn(ioDispatcher)
}
