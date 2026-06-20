package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MediaKeywordsResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywordsItem1
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieKeywordsResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toValidKeywordOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaKeywordsMapperTest {

  @Test
  fun toMediaKeywords_withValidValues_returnsMediaKeywords() {
    val movieMediaKeywords: MediaKeywords = movieKeywordsResponse.toMediaKeywords()
    assertEquals("crime", movieMediaKeywords.keywords?.get(0)?.name)
  }

  @Test
  fun toMediaKeywords_withNullValues_returnsVideoWithNullResults() {
    val movieMediaKeywords = MovieKeywordsResponse().toMediaKeywords()
    assertNull(movieMediaKeywords.keywords)
    assertNull(movieMediaKeywords.keywords?.get(0)?.name)
    assertNull(movieMediaKeywords.keywords?.get(0)?.id)

    val input: List<MediaKeywordsResponseItem>? = null
    val movieMediaKeywords2 = MovieKeywordsResponse(keywords = input).toMediaKeywords()
    assertEquals(null, movieMediaKeywords2.keywords)

    val movieWithNullItem =
      MovieKeywordsResponse(keywords = listOf(null)).toMediaKeywords()
    assertEquals(listOf(null), movieWithNullItem.keywords)
  }

  @Test
  fun toValidKeywordOrNull_withValidKeywords_returnsKeywordsDataCorrectly() {
    val result = mediaKeywordsItem1.toValidKeywordOrNull()
    assertEquals(result?.id, mediaKeywordsItem1.id)
    assertEquals(result?.name, mediaKeywordsItem1.name)
  }

  @Test
  fun toValidKeywordOrNull_withNullKeywords_returnNull() {
    val result = MediaKeywordsItem().toValidKeywordOrNull()
    assertNull(result)
  }

  @Test
  fun toValidKeywordOrNull_oneOfParametersIsNull_returnsNull() {
    val result = MediaKeywordsItem(id = 10).toValidKeywordOrNull()
    assertNull(result)

    val result2 = MediaKeywordsItem(name = "name").toValidKeywordOrNull()
    assertNull(result2)

    val result3 = MediaKeywordsItem(name = "").toValidKeywordOrNull()
    assertNull(result3)
  }
}
