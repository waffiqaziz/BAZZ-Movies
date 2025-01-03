package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GeneralHelperTest {

  @Test
  fun `test initLinearLayoutManagerVertical with valid context`() {
    val context: Context = RuntimeEnvironment.getApplication()
    val layoutManager = GeneralHelper.initLinearLayoutManagerVertical(context)

    assertEquals(LinearLayoutManager::class.java, layoutManager::class.java)
    assertEquals(LinearLayoutManager.VERTICAL, layoutManager.orientation)
    assertFalse(layoutManager.reverseLayout)
  }

  @Test
  fun `test initLinearLayoutManagerVertical with invalid context`() {
    // simulate a scenario with a mocked or invalid context
    val invalidContext: Context = RuntimeEnvironment.getApplication().applicationContext
    val layoutManager = GeneralHelper.initLinearLayoutManagerVertical(invalidContext)

    // verify the layout manager is created successfully
    assertNotNull(layoutManager)

    // verify orientation is vertical
    assertEquals(LinearLayoutManager.VERTICAL, layoutManager.orientation)

    // verify reverse layout is false
    assertFalse(layoutManager.reverseLayout)
  }

  @Test
  fun `test initLinearLayoutManagerVertical with unexpected LinearLayoutManager properties`() {
    val context: Context = RuntimeEnvironment.getApplication()
    val layoutManager = GeneralHelper.initLinearLayoutManagerVertical(context)

    // verify no unintended changes to other properties
    assertFalse(layoutManager.stackFromEnd) // default should be false
    assertEquals(LinearLayoutManager.VERTICAL, layoutManager.orientation)
  }
}
