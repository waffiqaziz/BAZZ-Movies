package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertNull
import org.junit.Test

class ImagePersonTest {

  @Test
  fun imagePerson_withNullValue_returnsDefaultValue() {
    val imagePeron = ImagePerson()

    assertNull(imagePeron.profiles)
    assertNull(imagePeron.id)
  }
}
