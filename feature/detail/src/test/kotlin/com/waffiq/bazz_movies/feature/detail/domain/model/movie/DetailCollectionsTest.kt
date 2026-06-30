package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DetailCollectionsTest {

  @Test
  fun createDetailCollections_withNoParameters_createsInstanceSuccessfully() {
    val collection = DetailCollections()

    assertNull(collection.id)
    assertNull(collection.name)
    assertNull(collection.originalName)
    assertNull(collection.overview)
    assertNull(collection.backdropPath)
    assertNull(collection.posterPath)
    assertNull(collection.originalLanguage)
    assertNull(collection.parts)
  }

  @Test
  fun createDetailCollections_withAllParameters_createsInstanceSuccessfully() {
    val collection = DetailCollections(
      id = 321,
      name = "name",
      originalName = "original name",
      overview = "overview",
      backdropPath = "backdrop path",
      posterPath = "poster path",
      originalLanguage = "original language",
      parts = listOf(PartsItem()),
    )

    assertNotNull(collection.id)
    assertNotNull(collection.name)
    assertNotNull(collection.originalName)
    assertNotNull(collection.overview)
    assertNotNull(collection.backdropPath)
    assertNotNull(collection.posterPath)
    assertNotNull(collection.originalLanguage)
    assertNotNull(collection.parts)
  }

  @Test
  fun getGenreIds_valid_returnsListOfGenre() {
    // setup with duplicate genres
    val data = DetailCollections(
      parts = listOf(
        PartsItem(genreIds = listOf(134, 34, 56)),
        PartsItem(genreIds = listOf(34, 45, 3)),
      ),
    )

    // should be sorted ascending and no duplicate for "34"
    assertEquals(listOf(3, 34, 45, 56, 134), data.genreIds)
  }

  @Test
  fun getGenreIds_partsIsNull_returnsNull() {
    val data = DetailCollections(parts = null)
    assertEquals(emptyList<Int>(), data.genreIds)
  }

  @Test
  fun getGenreIds_partsItemListNull_returnsNull() {
    val data = DetailCollections(parts = listOf(null))
    assertEquals(emptyList<Int>(), data.genreIds)
  }

  @Test
  fun getGenreIds_genreIdsIsNull_returnsNull() {
    val data = DetailCollections(parts = listOf(PartsItem(genreIds = null)))
    assertEquals(emptyList<Int>(), data.genreIds)
  }
}
