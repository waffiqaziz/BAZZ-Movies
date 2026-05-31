package com.waffiq.bazz_movies.core.database.data.model

data class FavoriteBackupEntry(
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
  fun isValid(): Boolean =
    mediaId > 0 &&
      mediaType.isNotBlank() &&
      title.isNotBlank()
}
