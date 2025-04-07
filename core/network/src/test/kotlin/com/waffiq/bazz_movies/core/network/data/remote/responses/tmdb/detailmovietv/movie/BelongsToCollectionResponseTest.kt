package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.belongsToCollectionDeadpoolDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class BelongsToCollectionResponseTest {

  @Test
  fun belongsToCollectionResponse_withValidValues_setsPropertiesCorrectly() {
    val belongsToCollectionResponse = belongsToCollectionDeadpoolDump
    assertEquals(448150, belongsToCollectionResponse.id)
    assertEquals("Deadpool Collection", belongsToCollectionResponse.name)
    assertEquals("/30c5jO7YEXuF8KiWXLg9m28GWDA.jpg", belongsToCollectionResponse.posterPath)
    assertEquals("/dTq7mGyAR5eAydR532feWfjJjzm.jpg", belongsToCollectionResponse.backdropPath)
  }

  @Test
  fun belongsToCollectionResponse_withDefaultValues_setsPropertiesCorrectly() {
    val belongsToCollectionResponse = BelongsToCollectionResponse()
    assertNull(belongsToCollectionResponse.name)
    assertNull(belongsToCollectionResponse.id)
    assertNull(belongsToCollectionResponse.posterPath)
    assertNull(belongsToCollectionResponse.backdropPath)
  }

  @Test
  fun belongsToCollectionResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val belongsToCollectionResponse = BelongsToCollectionResponse(
      name = "Transformers Collection",
      id = 246732
    )
    assertEquals("Transformers Collection", belongsToCollectionResponse.name)
    assertEquals(246732, belongsToCollectionResponse.id)
    assertNull(belongsToCollectionResponse.backdropPath)
    assertNull(belongsToCollectionResponse.posterPath)
  }
}
