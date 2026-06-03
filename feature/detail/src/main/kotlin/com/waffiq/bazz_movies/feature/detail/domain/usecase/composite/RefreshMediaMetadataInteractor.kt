package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.domain.repository.IFavoriteLocalDatabaseRepository
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.utils.mappers.BasicMediaDetailMapper.refreshWith
import javax.inject.Inject

class RefreshMediaMetadataInteractor @Inject constructor(
  private val getMediaDetailInteractor: GetMediaDetailInteractor,
  private val favoriteLocalDatabaseRepository: IFavoriteLocalDatabaseRepository,
) : RefreshMediaMetadataUseCase {

  override suspend fun refreshMedia(mediaId: Int, mediaType: String) {
    val favorite = favoriteLocalDatabaseRepository.getByMedia(mediaId, mediaType)
    if (favorite != null && !favorite.isStale()) {
      if (mediaType == MOVIE_MEDIA_TYPE) {
        refreshMovie(mediaId, favorite)
      } else {
        refreshTv(mediaId, favorite)
      }
    }
  }

  private suspend fun refreshMovie(mediaId: Int, favorite: Favorite) {
    getMediaDetailInteractor.getMovieDetailWithUserRegion(mediaId).collect {
      when (it) {
        is Outcome.Success -> {
          favoriteLocalDatabaseRepository.update(favorite.refreshWith(it.data))
        }

        else -> {
          // do nothing
        }
      }
    }
  }

  private suspend fun refreshTv(mediaId: Int, favorite: Favorite) {
    getMediaDetailInteractor.getTvDetailWithUserRegion(mediaId).collect {
      when (it) {
        is Outcome.Success -> {
          favoriteLocalDatabaseRepository.update(favorite.refreshWith(it.data))
        }

        else -> {
          // do nothing
        }
      }
    }
  }
}
