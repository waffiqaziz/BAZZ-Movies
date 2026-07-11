package com.waffiq.bazz_movies.navigation

import android.content.Intent
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class IntentExtrasTest {

  @Test
  fun extractParcelableExtraFromIntent_noExtra_returnsNull() {
    val intent = Intent()

    val result = extractParcelableExtraFromIntent<ListArgs>(intent, EXTRA_LIST)
    assertNull(result)
  }

  @Test
  @Config(sdk = [33])
  fun extractParcelableExtraFromIntent_exceptionThrown_returnsNull() {
    val intent: Intent = mockk()
    every { intent.setExtrasClassLoader(any()) } just Runs
    every { intent.getParcelableExtra(EXTRA_LIST, ListArgs::class.java) } throws
      ClassCastException("error class cast exception")

    val result = extractParcelableExtraFromIntent<ListArgs>(intent, EXTRA_LIST)

    assertNull(result)
  }

  @Test
  fun extractParcelableExtraFromIntent_wrongType_returnsNull() {
    val intent = Intent().apply {
      putExtra(EXTRA_LIST, "not a parcelable")
    }

    val result = extractParcelableExtraFromIntent<ListArgs>(intent, EXTRA_LIST)

    assertNull(result)
  }
}
