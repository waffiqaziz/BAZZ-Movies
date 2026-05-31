package com.waffiq.bazz_movies.core.database.data

import com.waffiq.bazz_movies.core.database.data.model.FavoriteBackupEntry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteBackupEntryTest {

  private fun createFavoriteBackupEntry(
    mediaId: Int = 1,
    mediaType: String = "movie",
    title: String = "Batman",
  ): FavoriteBackupEntry =
    FavoriteBackupEntry(
      mediaId = mediaId,
      mediaType = mediaType,
      genre = "Action",
      backDrop = "backdrop",
      poster = "poster",
      overview = "overview",
      title = title,
      releaseDate = "2025-01-01",
      popularity = 10.0,
      rating = 8.5f,
      isFavorite = true,
      isWatchlist = false,
      lastUpdated = 567,
    )

  @Test
  fun isValid_whenMediaIdIsZero_returnsFalse() {
    val entry = createFavoriteBackupEntry(mediaId = 0)

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenMediaIdIsNegative_returnsFalse() {
    val entry = createFavoriteBackupEntry(mediaId = -1)

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenMediaTypeIsBlank_returnsFalse() {
    val entry = createFavoriteBackupEntry(mediaType = "")

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenMediaTypeIsWhitespace_returnsFalse() {
    val entry = createFavoriteBackupEntry(mediaType = "   ")

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenTitleIsBlank_returnsFalse() {
    val entry = createFavoriteBackupEntry(title = "")

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenTitleIsWhitespace_returnsFalse() {
    val entry = createFavoriteBackupEntry(title = "   ")

    assertFalse(entry.isValid())
  }

  @Test
  fun isValid_whenAllRequiredFieldsAreValid_returnsTrue() {
    val entry = createFavoriteBackupEntry()

    assertTrue(entry.isValid())
  }
}
