package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.database.utils.LegacyGenreMapper.namesToIdString
import com.waffiq.bazz_movies.core.database.utils.LegacyGenreMapper.namesToIds
import org.junit.Assert.assertEquals
import org.junit.Test

class LegacyGenreMapperTest {

  @Test
  fun namesToIds_validString_retunrsStringOfId() {
    val result = namesToIds("Action, Unknown, \"\", Animation")
    assertEquals(result, listOf(28, 16))
  }

  @Test
  fun namesToIds_empty_retunrsEmpty() {
    val result = namesToIds("")
    assertEquals(result, listOf<Int>())
  }

  @Test
  fun namesToIdString_validString_retunrsStringOfId() {
    val result = namesToIdString("Action, Adventure, Animation")
    assertEquals(result, "28,12,16")
  }

  @Test
  fun namesToIds_numericId_returnsId() {
    val result = namesToIds("28")
    assertEquals(listOf(28), result)
  }
}
