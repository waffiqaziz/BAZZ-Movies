package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BelongsToCollectionTest {

  @Test
  fun createBelongsToCollection_withNoParameters_createsInstanceSuccessfully() {
    val collection = BelongsToCollection()

    assertNull(collection.backdropPath)
    assertNull(collection.name)
    assertNull(collection.id)
    assertNull(collection.posterPath)
  }

  @Test
  fun createBelongsToCollection_withAllParameters_createsInstanceSuccessfully() {
    val collection = BelongsToCollection(
      backdropPath = "/backdrop.jpg",
      name = "Marvel Collection",
      id = 123,
      posterPath = "/poster.jpg"
    )

    assertEquals("/backdrop.jpg", collection.backdropPath)
    assertEquals("Marvel Collection", collection.name)
    assertEquals(123, collection.id)
    assertEquals("/poster.jpg", collection.posterPath)
  }

  @Test
  fun createBelongsToCollection_withOnlyName_createsInstanceSuccessfully() {
    val collection = BelongsToCollection(name = "DC Collection")

    assertNull(collection.backdropPath)
    assertEquals("DC Collection", collection.name)
    assertNull(collection.id)
    assertNull(collection.posterPath)
  }

  @Test
  fun createBelongsToCollection_withExplicitNulls_createsInstanceSuccessfully() {
    val collection = BelongsToCollection(
      backdropPath = null,
      name = null,
      id = null,
      posterPath = null
    )

    assertNull(collection.backdropPath)
    assertNull(collection.name)
    assertNull(collection.id)
    assertNull(collection.posterPath)
  }
}
