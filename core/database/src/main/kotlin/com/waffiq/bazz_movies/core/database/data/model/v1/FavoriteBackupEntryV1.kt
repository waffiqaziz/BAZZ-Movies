package com.waffiq.bazz_movies.core.database.data.model.v1

import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import com.waffiq.bazz_movies.core.database.utils.LegacyGenreMapper
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteBackupEntryV1(
  val mediaId: Int,
  val mediaType: String,
  val genre: String,
  val backDrop: String,
  val poster: String,
  val overview: String,
  val title: String,
  val releaseDate: String,
  val popularity: Double,
  val rating: Float,
  val isFavorite: Boolean,
  val isWatchlist: Boolean,
  val lastUpdated: Long,
) {
  fun toCurrent() =
    FavoriteBackupEntry(
      mediaId = mediaId,
      mediaType = mediaType,
      genreIds = LegacyGenreMapper.namesToIds(genre),
      backDrop = backDrop,
      poster = poster,
      overview = overview,
      title = title,
      releaseDate = releaseDate,
      popularity = popularity,
      rating = rating,
      isFavorite = isFavorite,
      isWatchlist = isWatchlist,
      lastUpdated = lastUpdated,
    )
}
