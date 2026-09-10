package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.tvCastResponseItem1
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TvCastResponseItemTest {

  @Test
  fun tvCastResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(tvCastResponseItem1.totalEpisodeCount)
    assertNotNull(tvCastResponseItem1.gender)
    assertNotNull(tvCastResponseItem1.knownForDepartment)
    assertNotNull(tvCastResponseItem1.originalName)
    assertNotNull(tvCastResponseItem1.popularity)
    assertNotNull(tvCastResponseItem1.roles)
    assertNotNull(tvCastResponseItem1.name)
    assertNotNull(tvCastResponseItem1.profilePath)
    assertNotNull(tvCastResponseItem1.id)
    assertNotNull(tvCastResponseItem1.adult)
    assertNotNull(tvCastResponseItem1.order)
  }

  @Test
  fun tvCastResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = TvCastResponseItem()
    assertNull(nullData.totalEpisodeCount)
    assertNull(nullData.gender)
    assertNull(nullData.knownForDepartment)
    assertNull(nullData.originalName)
    assertNull(nullData.popularity)
    assertNull(nullData.roles)
    assertNull(nullData.name)
    assertNull(nullData.profilePath)
    assertNull(nullData.id)
    assertNull(nullData.adult)
    assertNull(nullData.order)
  }
}
