package com.waffiq.bazz_movies.feature.login.utils

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import com.waffiq.bazz_movies.feature.login.utils.InsetListener.applyWindowInsets
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WindowInsetsListenerTest {

  private val mockActivity: Activity = mock()
  private val mockResources: Resources = mock()
  private val mockConfiguration: Configuration = mock()
  private val mockView: View = mock()

  @Before
  fun setup() {
    `when`(mockActivity.resources).thenReturn(mockResources)
    `when`(mockResources.configuration).thenReturn(mockConfiguration)
  }

  @Test
  fun applyWindowInsetsListener_whenPortrait_returnsNulls() {
    mockConfiguration.orientation = Configuration.ORIENTATION_PORTRAIT
    assertNull(mockActivity.applyWindowInsets(mockView))
    verifyNoInteractions(mockView)
  }

  @Test
  fun applyWindowInsetsListener_whenLandscape_returnsNotNulls() {
    mockConfiguration.orientation = Configuration.ORIENTATION_LANDSCAPE
    assertNotNull(mockActivity.applyWindowInsets(mockView))
  }
}
