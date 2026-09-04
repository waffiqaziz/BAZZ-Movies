package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.jobsResponseItem
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class JobsResponseItemTest {

  @Test
  fun jobsResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(jobsResponseItem.episodeCount)
    assertNotNull(jobsResponseItem.creditId)
    assertNotNull(jobsResponseItem.job)
  }

  @Test
  fun jobsResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = JobsResponseItem()
    assertNull(nullData.episodeCount)
    assertNull(nullData.creditId)
    assertNull(nullData.job)
  }
}
