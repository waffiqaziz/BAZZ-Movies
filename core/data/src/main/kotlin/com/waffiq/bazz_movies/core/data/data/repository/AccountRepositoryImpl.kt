package com.waffiq.bazz_movies.core.data.data.repository

import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.data.domain.repository.IAccountRepository
import com.waffiq.bazz_movies.core.data.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.account.AccountRemoteDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
  private val accountRemoteDataSource: AccountRemoteDataSource,
) : IAccountRepository {

  override fun postFavorite(
    sessionId: String,
    fav: FavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    accountRemoteDataSource.postFavorite(sessionId, fav.toFavoriteRequest(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override fun postWatchlist(
    sessionId: String,
    wtc: WatchlistParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    accountRemoteDataSource.postWatchlist(sessionId, wtc.toWatchlistRequest(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }
}
