package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailCollectionsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.PartsResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailCollections
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toMediaArgs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionMapperTest {

  @Test
  fun toDetailCollections_withNullValues_returnsNull() {
    val detailCollectionResponse = DetailCollectionsResponse()

    val detailCollection = detailCollectionResponse.toDetailCollections()

    assertNull(detailCollection.backdropPath)
    assertNull(detailCollection.overview)
    assertNull(detailCollection.originalLanguage)
    assertNull(detailCollection.originalName)
    assertNull(detailCollection.name)
    assertNull(detailCollection.parts)
    assertNull(detailCollection.id)
    assertNull(detailCollection.posterPath)
  }

  @Test
  fun toDetailCollections_withNonNullPartsItem_returnsNull() {
    val detailCollectionResponse = DetailCollectionsResponse(parts = listOf(PartsResponseItem()))

    val detailCollection = detailCollectionResponse.toDetailCollections()

    assertEquals(listOf(PartsItem()), detailCollection.parts)
  }

  @Test
  fun toDetailCollections_emptyParts_returnsNull() {
    val detailCollectionsResponse = DetailCollectionsResponse(parts = emptyList())

    val detailCollection = detailCollectionsResponse.toDetailCollections()

    assertEquals(emptyList<PartsItem>(), detailCollection.parts)
  }

  @Test
  fun toDetailCollections_nullParts_returnsNull() {
    val detailCollectionsResponse = DetailCollectionsResponse(parts = listOf(null))

    val detailCollection = detailCollectionsResponse.toDetailCollections()

    assertNull(detailCollection.parts?.get(0))
  }

  @Test
  fun toDetailCollections_partsNonNull_returnsCorrectly() {
    val detailCollectionsResponse =
      DetailCollectionsResponse(parts = listOf(PartsResponseItem(id = 11)))

    val detailCollection = detailCollectionsResponse.toDetailCollections()

    assertEquals(11, detailCollection.parts?.get(0)?.id)
  }

  @Test
  fun toMediaArgs_nonNull_returnsCorrectly() {
    val partsItem = PartsItem(
      id = 131,
      video = true,
      mediaType = "tv",
      popularity = 5.0f,
      adult = true,
      voteCount = 434,
    )

    val mediaItem = partsItem.toMediaArgs()

    assertEquals(131, mediaItem.id)
    assertTrue(mediaItem.video)
    assertEquals(MediaType.TV, mediaItem.mediaType)
  }

  @Test
  fun toMediaArgs_nullValue_returnsDefault() {
    val partsItem = PartsItem()

    val mediaItem = partsItem.toMediaArgs()

    assertEquals(0, mediaItem.id)
    assertFalse(mediaItem.video)
    assertEquals(MediaType.MOVIE, mediaItem.mediaType)
  }
}
