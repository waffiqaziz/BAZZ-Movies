package com.waffiq.bazz_movies.core.database.utils

import androidx.annotation.VisibleForTesting
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName

object DatabaseMapper {

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun MediaItem.toFavorite(
    isFavorite: Boolean,
    isWatchlist: Boolean,
  ) = Favorite(
    id = 0,
    mediaId = id,
    mediaType = mediaType,
    title = name ?: originalName ?: title ?: originalTitle ?: NOT_AVAILABLE,
    releaseDate = releaseDate ?: firstAirDate ?: NOT_AVAILABLE,
    rating = voteAverage ?: 0.0f,
    backDrop = backdropPath ?: NOT_AVAILABLE,
    poster = posterPath ?: NOT_AVAILABLE,
    genre = transformListGenreIdsToJoinName(listGenreIds.orEmpty()),
    popularity = popularity ?: 0.0,
    overview = overview ?: NOT_AVAILABLE,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist
  )

  fun favTrueWatchlistTrue(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = true, isWatchlist = true)

  fun favTrueWatchlistFalse(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = true, isWatchlist = false)

  fun favFalseWatchlistTrue(data: MediaItem): Favorite =
    data.toFavorite(isFavorite = false, isWatchlist = true)

  fun FavoriteEntity.toFavorite() = Favorite(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    genre = genre,
    backDrop = backDrop,
    poster = poster,
    overview = overview,
    title = title,
    releaseDate = releaseDate,
    popularity = popularity,
    rating = rating,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist,
  )

  fun Favorite.toFavoriteEntity() = FavoriteEntity(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    genre = genre,
    backDrop = backDrop,
    poster = poster,
    overview = overview,
    title = title,
    releaseDate = releaseDate,
    popularity = popularity,
    rating = rating,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist,
  )
}
