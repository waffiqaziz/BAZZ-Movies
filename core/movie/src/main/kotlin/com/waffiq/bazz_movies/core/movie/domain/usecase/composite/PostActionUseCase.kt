package com.waffiq.bazz_movies.core.movie.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface PostActionUseCase {
  fun postWatchlistWithAuth(
    wtc: WatchlistModel,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postFavoriteWithAuth(
    fav: FavoriteModel
  ): Flow<Outcome<PostFavoriteWatchlist>>
}
