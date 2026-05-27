package com.waffiq.bazz_movies.feature.more.utils

import com.waffiq.bazz_movies.feature.more.utils.Helper.validName
import org.junit.Assert.assertEquals
import org.junit.Test

class HelperTest {

  @Test
  fun validName_whenNameIsValid_returnsTheName() {
    assertEquals("Name not set", "".validName("Name not set"))
  }

  @Test
  fun validName_whenNameIsEmpty_returnsFallback() {
    assertEquals("Name User", "Name User".validName(""))
  }
}
