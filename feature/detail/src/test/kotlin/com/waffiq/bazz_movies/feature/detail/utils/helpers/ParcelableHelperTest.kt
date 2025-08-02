package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Intent
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity.Companion.EXTRA_MOVIE
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ParcelableHelper.extractMediaItemFromIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ParcelableHelperTest {

  private val mediaItem = MediaItem(id = 1, title = "Test Movie")

  @Test
  @Config(sdk = [30])
  fun extractMediaItem_preTiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_MOVIE, mediaItem)
    }

    val result = extractMediaItemFromIntent(intent)
    assertEquals(mediaItem, result)
  }

  @Test
  @Config(sdk = [33]) // Android 13, TIRAMISU+
  fun extractMediaItem_tiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_MOVIE, mediaItem)
    }

    val result = extractMediaItemFromIntent(intent)
    assertEquals(mediaItem, result)
  }

  @Test
  fun extractMediaItem_noExtra_returnsNull() {
    val intent = Intent() // no extras

    val result = extractMediaItemFromIntent(intent)
    assertNull(result)
  }
}
