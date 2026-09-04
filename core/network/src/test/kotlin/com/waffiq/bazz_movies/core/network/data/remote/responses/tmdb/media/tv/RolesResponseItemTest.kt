package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.rolesResponseItem
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RolesResponseItemTest {

  @Test
  fun rolesResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(rolesResponseItem.character)
    assertNotNull(rolesResponseItem.episodeCount)
    assertNotNull(rolesResponseItem.creditId)
  }

  @Test
  fun rolesResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = RolesResponseItem()
    assertNull(nullData.character)
    assertNull(nullData.episodeCount)
    assertNull(nullData.creditId)
  }
}
