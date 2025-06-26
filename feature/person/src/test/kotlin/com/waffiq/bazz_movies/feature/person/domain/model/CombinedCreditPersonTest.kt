package com.waffiq.bazz_movies.feature.person.domain.model

import junit.framework.TestCase.assertNull
import org.junit.Test

class CombinedCreditPersonTest {

  @Test
  fun combinedCreditPerson_withNullValue_returnsDefaultValue() {
    val combinedCreditPerson = CombinedCreditPerson()

    assertNull(combinedCreditPerson.id)
    assertNull(combinedCreditPerson.cast)
    assertNull(combinedCreditPerson.crew)
  }
}
