package com.waffiq.bazz_movies.feature.detail.utils.mapper

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCastItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaCastItem
import com.waffiq.bazz_movies.feature.detail.utils.mapper.PersonArgsMappers.toPersonArgs
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonArgsMappersTest {

  @Test
  fun toPersonArgs_withValidValue_returnsCorrectly() {
    val result = mediaCastItem.toPersonArgs()
    assertEquals(mediaCastItem.id, result.id)
    assertEquals(mediaCastItem.name, result.name)
    assertEquals(mediaCastItem.originalName, result.originalName)
    assertEquals(mediaCastItem.profilePath, result.profilePath)
  }

  @Test
  fun toPersonArgs_invalidValue_returnsCorrectly() {
    val result = MediaCastItem().toPersonArgs()
    assertEquals(0, result.id)
  }
}
