package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName

object FavoriteMapper {

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
      lastUpdated = System.currentTimeMillis(), // use latest date every time
    )

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
      lastUpdated,
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
      lastUpdated,
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
      lastUpdated,
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
      lastUpdated,
    )
}
