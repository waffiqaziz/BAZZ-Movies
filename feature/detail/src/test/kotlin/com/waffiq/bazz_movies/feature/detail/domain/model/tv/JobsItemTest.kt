package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.jobsItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class JobsItemTest {

  @Test
  fun jobsItem_withValidValues_getsPropertiesCorrectly() {
    assertEquals("Writter", jobsItem.job)
    assertEquals(12, jobsItem.episodeCount)
    assertEquals("creditId", jobsItem.creditId)
  }

  @Test
  fun jobsItem_withDefaultValues_getsPropertiesCorrectly() {
    val jobsItem = JobsItem()
    assertNull(jobsItem.episodeCount)
    assertNull(jobsItem.creditId)
    assertNull(jobsItem.job)
  }
}
