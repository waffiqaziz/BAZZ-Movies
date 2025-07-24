package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PostModelStateTest {

  @Test
  fun createPostModelState_withAllTrue_createsInstanceSuccessfully() {
    val postState = PostModelState(
      isSuccess = true,
      isDelete = true,
      isFavorite = true
    )

    assertTrue(postState.isSuccess)
    assertTrue(postState.isDelete)
    assertTrue(postState.isFavorite)
  }

  @Test
  fun createPostModelState_withAllFalse_createsInstanceSuccessfully() {
    val postState = PostModelState(
      isSuccess = false,
      isDelete = false,
      isFavorite = false
    )

    assertFalse(postState.isSuccess)
    assertFalse(postState.isDelete)
    assertFalse(postState.isFavorite)
  }

  @Test
  fun createPostModelState_withMixedValues_createsInstanceSuccessfully() {
    val postState = PostModelState(
      isSuccess = true,
      isDelete = false,
      isFavorite = true
    )

    assertTrue(postState.isSuccess)
    assertFalse(postState.isDelete)
    assertTrue(postState.isFavorite)
  }

  @Test
  fun createPostModelState_withOppositeValues_createsInstanceSuccessfully() {
    val postState = PostModelState(
      isSuccess = false,
      isDelete = true,
      isFavorite = false
    )

    assertFalse(postState.isSuccess)
    assertTrue(postState.isDelete)
    assertFalse(postState.isFavorite)
  }
}
