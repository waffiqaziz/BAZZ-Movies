package com.waffiq.bazz_movies.core.data.domain.repository

import com.waffiq.bazz_movies.core.common.Outcome
import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.model.user.FavoriteParams
import com.waffiq.bazz_movies.core.model.user.WatchlistParams
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {
  fun postFavorite(
    sessionId: String,
    fav: FavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postWatchlist(
    sessionId: String,
    wtc: WatchlistParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>
}
