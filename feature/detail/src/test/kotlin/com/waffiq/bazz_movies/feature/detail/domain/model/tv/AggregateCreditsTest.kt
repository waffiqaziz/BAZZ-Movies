package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.aggregateCredits
import org.junit.Assert.assertNotNull
import org.junit.Test

class AggregateCreditsTest {

  @Test
  fun aggregateCredits_withValidValues_gestPropertiesCorrectly() {
    assertNotNull(aggregateCredits.cast.first())
    assertNotNull(aggregateCredits.crew.first())
  }
}
