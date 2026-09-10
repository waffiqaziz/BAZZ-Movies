package com.waffiq.bazz_movies.core.data.domain.repository

import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.model.FavoriteParams
import com.waffiq.bazz_movies.core.model.Outcome
import com.waffiq.bazz_movies.core.model.WatchlistParams
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
