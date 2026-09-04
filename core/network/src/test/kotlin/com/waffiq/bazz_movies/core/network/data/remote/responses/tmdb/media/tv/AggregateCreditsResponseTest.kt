package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.aggregateCreditsResponse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AggregateCreditsResponseTest {

  @Test
  fun agregateCreditsResponse_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(aggregateCreditsResponse.cast)
    assertNotNull(aggregateCreditsResponse.crew)
  }

  @Test
  fun agregateCreditsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = AggregateCreditsResponse()
    assertNull(nullData.cast)
    assertNull(nullData.crew)
  }
}
