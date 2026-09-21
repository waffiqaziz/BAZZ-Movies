package com.waffiq.bazz_movies.core.database.data.model.v1

import org.junit.Assert.assertNull
import org.junit.Test

class DatabaseBackupV1Test {

  private val favorite = FavoriteBackupEntryV1(
    mediaId = 123,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop",
    poster = "poster",
    overview = "overview",
    title = "title",
    releaseDate = "releaseDate",
    popularity = 900.0,
    rating = 9.0f,
    isFavorite = false,
    isWatchlist = false,
    lastUpdated = 0,
  )

  @Test
  fun databaseBackupV1_checksumNull_retunsValueCorrectly() {
    val databaseBackup = DatabaseBackupV1(
      appVersion = "1.5.0",
      favorites = listOf(favorite),
      version = 1,
      createdAt = 43232,
    )
    assertNull(databaseBackup.checksum)
  }
}
