package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Intent
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity.Companion.EXTRA_PERSON
import com.waffiq.bazz_movies.feature.person.utils.helper.ParcelableHelper.extractMediaCastItemFromIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ParcelableHelperTest {

  private val mediaCastItem = MediaCastItem(id = 1, name = "Person Name")

  @Test
  @Config(sdk = [30])
  fun extractMediaCastItem_preTiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_PERSON, mediaCastItem)
    }

    val result = extractMediaCastItemFromIntent(intent)
    assertEquals(mediaCastItem, result)
  }

  @Test
  @Config(sdk = [33]) // Android 13, TIRAMISU+
  fun extractMediaCastItem_tiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_PERSON, mediaCastItem)
    }

    val result = extractMediaCastItemFromIntent(intent)
    assertEquals(mediaCastItem, result)
  }

  @Test
  fun extractMediaCastItem_noExtra_returnsNull() {
    val intent = Intent() // no extras

    val result = extractMediaCastItemFromIntent(intent)
    assertNull(result)
  }
}
