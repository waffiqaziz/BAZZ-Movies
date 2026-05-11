package com.waffiq.bazz_movies.core.data.domain.usecase.composite

import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.models.WatchlistParams
import kotlinx.coroutines.flow.Flow

interface PostActionUseCase {
  fun postWatchlistWithAuth(wtc: WatchlistParams): Flow<Outcome<PostFavoriteWatchlist>>
  fun postFavoriteWithAuth(fav: FavoriteParams): Flow<Outcome<PostFavoriteWatchlist>>
}
