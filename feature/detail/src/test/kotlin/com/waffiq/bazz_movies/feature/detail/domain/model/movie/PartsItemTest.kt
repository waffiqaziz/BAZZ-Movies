package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.partsItem
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PartsItemTest {

  @Test
  fun partsItem_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(partsItem.id)
    assertNotNull(partsItem.title)
    assertNotNull(partsItem.originalTitle)
    assertNotNull(partsItem.posterPath)
    assertNotNull(partsItem.backdropPath)
    assertNotNull(partsItem.overview)
    assertNotNull(partsItem.originalLanguage)
    assertNotNull(partsItem.video)
    assertNotNull(partsItem.genreIds)
    assertNotNull(partsItem.mediaType)
    assertNotNull(partsItem.releaseDate)
    assertNotNull(partsItem.popularity)
    assertNotNull(partsItem.voteAverage)
    assertNotNull(partsItem.adult)
    assertNotNull(partsItem.voteCount)
  }

  @Test
  fun partsItem_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = PartsItem()
    assertNull(nullData.id)
    assertNull(nullData.title)
    assertNull(nullData.originalTitle)
    assertNull(nullData.posterPath)
    assertNull(nullData.backdropPath)
    assertNull(nullData.overview)
    assertNull(nullData.originalLanguage)
    assertNull(nullData.video)
    assertNull(nullData.genreIds)
    assertNull(nullData.mediaType)
    assertNull(nullData.releaseDate)
    assertNull(nullData.popularity)
    assertNull(nullData.voteAverage)
    assertNull(nullData.adult)
    assertNull(nullData.voteCount)
  }
}
