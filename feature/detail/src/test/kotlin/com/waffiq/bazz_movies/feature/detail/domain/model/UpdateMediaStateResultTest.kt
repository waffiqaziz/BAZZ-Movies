package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateMediaStateResultTest {

  @Test
  fun createUpdateMediaStateResult_withAllTrue_createsInstanceSuccessfully() {
    val mediaStateResult = UpdateMediaStateResult(
      isSuccess = true,
      isDelete = true,
      isFavorite = true
    )

    assertTrue(mediaStateResult.isSuccess)
    assertTrue(mediaStateResult.isDelete)
    assertTrue(mediaStateResult.isFavorite)
  }

  @Test
  fun createUpdateMediaStateResult_withAllFalse_createsInstanceSuccessfully() {
    val mediaStateResult = UpdateMediaStateResult(
      isSuccess = false,
      isDelete = false,
      isFavorite = false
    )

    assertFalse(mediaStateResult.isSuccess)
    assertFalse(mediaStateResult.isDelete)
    assertFalse(mediaStateResult.isFavorite)
  }

  @Test
  fun createUpdateMediaStateResult_withMixedValues_createsInstanceSuccessfully() {
    val mediaStateResult = UpdateMediaStateResult(
      isSuccess = true,
      isDelete = false,
      isFavorite = true
    )

    assertTrue(mediaStateResult.isSuccess)
    assertFalse(mediaStateResult.isDelete)
    assertTrue(mediaStateResult.isFavorite)
  }

  @Test
  fun createUpdateMediaStateResult_withOppositeValues_createsInstanceSuccessfully() {
    val mediaStateResult = UpdateMediaStateResult(
      isSuccess = false,
      isDelete = true,
      isFavorite = false
    )

    assertFalse(mediaStateResult.isSuccess)
    assertTrue(mediaStateResult.isDelete)
    assertFalse(mediaStateResult.isFavorite)
  }
}
