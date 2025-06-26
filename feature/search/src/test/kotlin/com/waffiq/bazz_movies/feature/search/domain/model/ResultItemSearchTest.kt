package com.waffiq.bazz_movies.feature.search.domain.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import org.junit.Test

class ResultItemSearchTest {

  @Test
  fun resultItemSearch_withNullValue_returnDefaultValue(){
    val resultsItemSearch = ResultsItemSearch(id = 1)

    assertEquals(resultsItemSearch.mediaType, "movie")
    assertNull(resultsItemSearch.listKnownFor)
    assertNull(resultsItemSearch.knownForDepartment)
    assertEquals(resultsItemSearch.popularity, 0.0)
    assertNull(resultsItemSearch.name)
    assertNull(resultsItemSearch.profilePath)
    assertEquals(resultsItemSearch.id, 1)
    assertFalse(resultsItemSearch.adult)
    assertNull(resultsItemSearch.overview)
    assertNull(resultsItemSearch.originalLanguage)
    assertNull(resultsItemSearch.originalTitle)
    assertFalse(resultsItemSearch.video)
    assertNull(resultsItemSearch.title)
    assertNull(resultsItemSearch.listGenreIds)
    assertNull(resultsItemSearch.posterPath)
    assertNull(resultsItemSearch.backdropPath)
    assertNull(resultsItemSearch.releaseDate)
    assertEquals(resultsItemSearch.voteAverage, 0.0)
    assertEquals(resultsItemSearch.voteCount, 0.0)
    assertNull(resultsItemSearch.firstAirDate)
    assertNull(resultsItemSearch.listOriginCountry)
    assertNull(resultsItemSearch.originalName)
  }
}
