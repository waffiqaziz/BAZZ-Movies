package com.waffiq.bazz_movies.feature.login.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.feature.login.utils.Helper.loadTypeface
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HelperTest {

  private val context = ApplicationProvider.getApplicationContext<Context>()

  @Test
  fun loadTypeface_whenResourceNull_fallsBackToDefault() {
    mockkStatic(ResourcesCompat::class)
    every { ResourcesCompat.getFont(context, any()) } returns null

    val result = context.loadTypeface(nunito_sans_regular)

    assertNotNull(result) // Typeface.DEFAULT is now real
  }

  @Test
  fun loadTypeface_whenResourceFound_returnsTypeface() {
    val mockTypeface = mockk<Typeface>()
    mockkStatic(ResourcesCompat::class)
    every { ResourcesCompat.getFont(context, any()) } returns mockTypeface

    val result = context.loadTypeface(nunito_sans_regular)

    assertEquals(mockTypeface, result)
  }
}
