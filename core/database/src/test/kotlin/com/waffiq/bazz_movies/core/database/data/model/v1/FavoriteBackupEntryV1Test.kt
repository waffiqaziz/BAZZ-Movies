package com.waffiq.bazz_movies.core.database.data.model.v1

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import kotlin.test.Test

class FavoriteBackupEntryV1Test {

  private val json = Json {
    ignoreUnknownKeys = true
  }

  private val entry = FavoriteBackupEntryV1(
    mediaId = 123,
    mediaType = "movie",
    genre = "Action, Comedy",
    backDrop = "backdrop.jpg",
    poster = "poster.jpg",
    overview = "A movie overview",
    title = "Test Movie",
    releaseDate = "2026-01-01",
    popularity = 123.45,
    rating = 8.5f,
    isFavorite = true,
    isWatchlist = false,
    lastUpdated = 1_725_000_000_000L,
  )

  @Test
  fun favoriteBackupEntryV1_validEntry_returnsExpectedValues() {
    assertEquals(123, entry.mediaId)
    assertEquals("movie", entry.mediaType)
    assertEquals("Action, Comedy", entry.genre)
    assertEquals("backdrop.jpg", entry.backDrop)
    assertEquals("poster.jpg", entry.poster)
    assertEquals("A movie overview", entry.overview)
    assertEquals("Test Movie", entry.title)
    assertEquals("2026-01-01", entry.releaseDate)
    assertEquals(123.45, entry.popularity, 0.0)
    assertEquals(8.5f, entry.rating)
    assertTrue(entry.isFavorite)
    assertFalse(entry.isWatchlist)
    assertEquals(1_725_000_000_000L, entry.lastUpdated)
  }

  @Test
  fun databaseBackupV1_validEntry_shouldSerializeAndDeserializeBackup() {
    val backup = DatabaseBackupV1(
      version = 1,
      createdAt = 1_725_000_000_000L,
      appVersion = "1.5.1-dev",
      favorites = listOf(entry),
      checksum = "abc123",
    )

    val jsonString = json.encodeToString(backup)
    val result = json.decodeFromString<DatabaseBackupV1>(jsonString)

    assertEquals(backup, result)
  }

  @Test
  fun backupPayloadV1_validEntriy_returnsExpectedValues() {
    val favorites = emptyList<FavoriteBackupEntryV1>()

    val payload = BackupPayloadV1(
      version = 1,
      createdAt = 1_725_000_000_000L,
      appVersion = "1.5.1-dev",
      favorites = favorites,
    )

    assertEquals(1, payload.version)
    assertEquals(1_725_000_000_000L, payload.createdAt)
    assertEquals("1.5.1-dev", payload.appVersion)
    assertEquals(favorites, payload.favorites)
  }
}
