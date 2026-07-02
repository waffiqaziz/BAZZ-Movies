package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie

import org.junit.Assert.assertNull
import org.junit.Test

class DetailCollectionsResponseTest {

  @Test
  fun detailCollectionsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val detailCollectionsResponse = DetailCollectionsResponse()
    assertNull(detailCollectionsResponse.backdropPath)
    assertNull(detailCollectionsResponse.overview)
    assertNull(detailCollectionsResponse.originalLanguage)
    assertNull(detailCollectionsResponse.originalName)
    assertNull(detailCollectionsResponse.name)
    assertNull(detailCollectionsResponse.parts)
    assertNull(detailCollectionsResponse.id)
    assertNull(detailCollectionsResponse.posterPath)
  }
}
