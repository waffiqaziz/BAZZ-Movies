package com.waffiq.bazz_movies.feature.list.utils

import android.content.Intent
import android.os.BadParcelableException
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.movieKeywordsArgs
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.feature.list.utils.ParcelableHelper.extractArgsItemFromIntent
import com.waffiq.bazz_movies.navigation.ListArgs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
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

  @Test
  fun extractArgsItemFromIntent_whenBadParcelableThrown_returnsNull() {
    val intent = mock(Intent::class.java)

    @Suppress("DEPRECATION")
    whenever(intent.getParcelableExtra<ListArgs>(EXTRA_LIST))
      .thenThrow(BadParcelableException("error bad parcelable exception"))

    val result = extractArgsItemFromIntent(intent)

    assertNull(result)
  }

  @Test
  @Config(sdk = [33])
  fun extractArgsItemFromIntent_tiramisu_whenBadParcelableThrown_returnsNull() {
    val intent = mock(Intent::class.java)

    whenever(intent.getParcelableExtra(EXTRA_LIST, ListArgs::class.java))
      .thenThrow(BadParcelableException("error bad parcelable exception"))

    val result = extractArgsItemFromIntent(intent)

    assertNull(result)
  }

  @Test
  fun extractArgsItemFromIntent_whenClassCastThrown_returnsNull() {
    val intent = mock(Intent::class.java)

    @Suppress("DEPRECATION")
    whenever(intent.getParcelableExtra<ListArgs>(EXTRA_LIST))
      .thenThrow(ClassCastException())

    val result = extractArgsItemFromIntent(intent)

    assertNull(result)
  }

  @Test
  @Config(sdk = [33])
  fun extractArgsItemFromIntent_tiramisu_whenClassCastThrown_returnsNull() {
    val intent = mock(Intent::class.java)

    whenever(intent.getParcelableExtra(EXTRA_LIST, ListArgs::class.java))
      .thenThrow(ClassCastException("error class cast exception"))

    val result = extractArgsItemFromIntent(intent)

    assertNull(result)
  }

  @Test
  fun extractArgsItemFromIntent_wrongType_returnsNull() {
    val intent = Intent().apply {
      putExtra(EXTRA_LIST, "not a parcelable")
    }

    val result = extractArgsItemFromIntent(intent)

    assertNull(result)
  }
}
