package com.waffiq.bazz_movies.feature.person.domain.model

import junit.framework.TestCase.assertNull
import org.junit.Test

class CrewItemTest {

  @Test
  fun crewItem_withNullValue_returnsDefaultValue() {
    val crewItem = CrewItem()

    assertNull(crewItem.overview)
    assertNull(crewItem.originalLanguage)
    assertNull(crewItem.originalTitle)
    assertNull(crewItem.video)
    assertNull(crewItem.title)
    assertNull(crewItem.genreIds)
    assertNull(crewItem.posterPath)
    assertNull(crewItem.backdropPath)
    assertNull(crewItem.releaseDate)
    assertNull(crewItem.creditId)
    assertNull(crewItem.mediaType)
    assertNull(crewItem.popularity)
    assertNull(crewItem.voteAverage)
    assertNull(crewItem.id)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
    assertNull(crewItem.job)
    assertNull(crewItem.voteCount)
  }
}
