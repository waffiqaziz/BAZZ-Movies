package com.waffiq.bazz_movies.core.database.utils

import androidx.annotation.VisibleForTesting
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName

object FavoriteMapper {

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun MediaItem.toFavorite(isFavorite: Boolean, isWatchlist: Boolean) =
    Favorite(
      id = 0,
      mediaId = id,
      mediaType = mediaType,
      title = name ?: originalName ?: title ?: originalTitle ?: NOT_AVAILABLE,
      releaseDate = releaseDate ?: firstAirDate ?: NOT_AVAILABLE,
      rating = voteAverage ?: 0.0f,
      backDrop = backdropPath ?: NOT_AVAILABLE,
      poster = posterPath ?: NOT_AVAILABLE,
      genre = transformListGenreIdsToJoinName(listGenreIds.orEmpty()).orEmpty(),
      popularity = popularity ?: 0.0,
      overview = overview ?: NOT_AVAILABLE,
      isFavorite = isFavorite,
      isWatchlist = isWatchlist,
    )

  fun favTrueWatchlistTrue(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = true, isWatchlist = true)

  fun favTrueWatchlistFalse(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = true, isWatchlist = false)

  fun favFalseWatchlistTrue(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = false, isWatchlist = true)

  fun FavoriteEntity.toFavorite() =
    Favorite(
      id,
      mediaId,
      mediaType,
      genre,
      backDrop,
      poster,
      overview,
      title,
      releaseDate,
      popularity,
      rating,
      isFavorite,
      isWatchlist,
    )

  fun Favorite.toFavoriteEntity() =
    FavoriteEntity(
      id,
      mediaId,
      mediaType,
      genre,
      backDrop,
      poster,
      overview,
      title,
      releaseDate,
      popularity,
      rating,
      isFavorite,
      isWatchlist,
    )

  fun FavoriteEntity.toBackupEntry() =
    FavoriteBackupEntry(
      mediaId,
      mediaType,
      genre,
      backDrop,
      poster,
      overview,
      title,
      releaseDate,
      popularity,
      rating,
      isFavorite,
      isWatchlist,
    )

  fun FavoriteBackupEntry.toEntity() =
    FavoriteEntity(
      id = 0,
      mediaId,
      mediaType,
      genre,
      backDrop,
      poster,
      overview,
      title,
      releaseDate,
      popularity,
      rating,
      isFavorite,
      isWatchlist,
    )
}
