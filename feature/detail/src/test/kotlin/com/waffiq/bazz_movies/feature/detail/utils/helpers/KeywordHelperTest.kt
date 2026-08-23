package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getListOfKeywords
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class KeywordHelperTest {

  @Test
  fun getListOfKeywords_whenListIsNull_returnsNull() {
    val result = getListOfKeywords(null)
    assertNull(result)
  }

  @Test
  fun getListOfKeywords_whenListIsEmpty_returnsEmptyList() {
    val result = getListOfKeywords(emptyList())
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenItemIsNull_returnsEmptyList() {
    val result = getListOfKeywords(listOf(null))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenIdIsNull_returnsEmptyList() {
    val item = MediaKeywordsItem(id = null, name = "action")
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenNameIsNull_returnsEmptyList() {
    val item = MediaKeywordsItem(id = 1, name = null)
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenNameIsEmpty_returnsEmptyList() {
    val item = MediaKeywordsItem(id = 1, name = "")
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenIdAndNameAreValid_returnsFilteredList() {
    val item = MediaKeywordsItem(id = 1, name = "action")
    val result = getListOfKeywords(listOf(item))
    assertEquals(listOf(item), result)
  }

  @Test
  fun getListOfKeywords_whenMixedValidAndInvalidItems_returnsOnlyValidItems() {
    val validItem1 = MediaKeywordsItem(id = 1, name = "action")
    val validItem2 = MediaKeywordsItem(id = 2, name = "comedy")
    val nullItem = null
    val nullIdItem = MediaKeywordsItem(id = null, name = "drama")
    val nullNameItem = MediaKeywordsItem(id = 4, name = null)
    val emptyNameItem = MediaKeywordsItem(id = 5, name = "")

    val result = getListOfKeywords(
      listOf(validItem1, nullItem, nullIdItem, nullNameItem, emptyNameItem, validItem2),
    )

    assertEquals(listOf(validItem1, validItem2), result)
  }
}
