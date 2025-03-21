package com.waffiq.bazz_movies.feature.search.domain.model

import junit.framework.TestCase.assertNull
import org.junit.Test

class KnownForItemTest {

  @Test
  fun knownForItem_nullValue_returnDefaultValue() {
    val knownForItem = KnownForItem()

    assertNull(knownForItem.overview)
    assertNull(knownForItem.originalLanguage)
    assertNull(knownForItem.originalTitle)
    assertNull(knownForItem.video)
    assertNull(knownForItem.title)
    assertNull(knownForItem.genreIds)
    assertNull(knownForItem.posterPath)
    assertNull(knownForItem.backdropPath)
    assertNull(knownForItem.releaseDate)
    assertNull(knownForItem.mediaType)
    assertNull(knownForItem.popularity)
    assertNull(knownForItem.voteAverage)
    assertNull(knownForItem.id)
    assertNull(knownForItem.adult)
    assertNull(knownForItem.voteCount)
    assertNull(knownForItem.firstAirDate)
    assertNull(knownForItem.originCountry)
    assertNull(knownForItem.originalName)
    assertNull(knownForItem.name)
  }
}
