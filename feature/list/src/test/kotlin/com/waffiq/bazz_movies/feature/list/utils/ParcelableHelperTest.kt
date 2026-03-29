package com.waffiq.bazz_movies.feature.list.utils

import android.content.Intent
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.movieKeywordsArgs
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.feature.list.utils.ParcelableHelper.extractArgsItemFromIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ParcelableHelperTest {

  @Test
  @Config(sdk = [30])
  fun extractArgsItemFromIntent_preTiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_LIST, movieKeywordsArgs)
    }

    val result = extractArgsItemFromIntent(intent)
    assertEquals(movieKeywordsArgs, result)
  }

  @Test
  @Config(sdk = [33]) // Android 13, TIRAMISU+
  fun extractArgsItemFromIntent_tiramisu_returnsItem() {
    val intent = Intent().apply {
      putExtra(EXTRA_LIST, movieKeywordsArgs)
    }

    val result = extractArgsItemFromIntent(intent)
    assertEquals(movieKeywordsArgs, result)
  }

  @Test
  fun extractArgsItemFromIntent_noExtra_returnsNull() {
    val intent = Intent() // no extras

    val result = extractArgsItemFromIntent(intent)
    assertNull(result)
  }
}
