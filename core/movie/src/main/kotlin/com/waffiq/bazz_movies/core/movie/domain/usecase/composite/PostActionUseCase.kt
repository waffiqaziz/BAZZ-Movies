package com.waffiq.bazz_movies.core.movie.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface PostActionUseCase {
  fun postWatchlistWithAuth(
    wtc: UpdateWatchlistParams,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postFavoriteWithAuth(
    fav: UpdateFavoriteParams
  ): Flow<Outcome<PostFavoriteWatchlist>>
}
