package com.waffiq.bazz_movies.feature.list.utils

import com.waffiq.bazz_movies.feature.list.utils.Helper.capitaliseEachWord
import org.junit.Assert.assertEquals
import org.junit.Test

class CapitalEachWordTest {

  @Test
  fun capitalEachWordTest_withValidString_returnsCorrectly() {
    assertEquals(
      "This Is The NaMe OF The TiTTLE",
      "This is the NaMe OF the TiTTLE".capitaliseEachWord(),
    )
  }
}
