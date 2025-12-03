package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface PostMethodWithUserUseCase {
  fun postFavorite(fav: FavoriteModel): Flow<Outcome<PostFavoriteWatchlist>>
  fun postWatchlist(wtc: WatchlistModel): Flow<Outcome<PostFavoriteWatchlist>>
  fun postMovieRate(rating: Float, movieId: Int): Flow<Outcome<Post>>
  fun postTvRate(rating: Float, tvId: Int): Flow<Outcome<Post>>
}
