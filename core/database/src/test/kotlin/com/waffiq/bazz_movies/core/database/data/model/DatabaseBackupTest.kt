package com.waffiq.bazz_movies.core.database.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseBackupTest {

  val favorite = FavoriteBackupEntry(
    mediaId = 123,
    mediaType = "movie",
    genre = "Animation",
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
  fun databaseBackup_defaultValue_shouldReturnValueCorrectly() {
    val databaseBackup = DatabaseBackup(
      createdAt = 123123,
      favorites = listOf(favorite),
      version = 1,
      appVersion = "",
      checksum = "1234",
    )

    assertEquals(databaseBackup.favorites[0], favorite)
    assertEquals(databaseBackup.version, 1)
    assertEquals(databaseBackup.appVersion, "")
    assertEquals(databaseBackup.createdAt, 123123)
  }

  @Test
  fun databaseBackup_checksumNull_retunsValueCorrectly() {
    val databaseBackup = DatabaseBackup(
      appVersion = "1.5.0",
      favorites = listOf(favorite),
      version = 1,
      createdAt = 43232,
    )

    assertEquals(databaseBackup.appVersion, "1.5.0")
  }
}
