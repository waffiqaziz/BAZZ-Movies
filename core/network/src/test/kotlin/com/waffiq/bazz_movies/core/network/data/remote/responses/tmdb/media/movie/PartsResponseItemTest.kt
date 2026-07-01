package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie

import org.junit.Assert.assertNull
import org.junit.Test

class PartsResponseItemTest {

  @Test
  fun partsResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val partsResponseItem = PartsResponseItem()
    assertNull(partsResponseItem.overview)
    assertNull(partsResponseItem.originalLanguage)
    assertNull(partsResponseItem.video)
    assertNull(partsResponseItem.genreIds)
    assertNull(partsResponseItem.posterPath)
    assertNull(partsResponseItem.backdropPath)
    assertNull(partsResponseItem.mediaType)
    assertNull(partsResponseItem.releaseDate)
    assertNull(partsResponseItem.originalTitle)
    assertNull(partsResponseItem.popularity)
    assertNull(partsResponseItem.voteAverage)
    assertNull(partsResponseItem.title)
    assertNull(partsResponseItem.id)
    assertNull(partsResponseItem.adult)
    assertNull(partsResponseItem.voteCount)
  }
}
