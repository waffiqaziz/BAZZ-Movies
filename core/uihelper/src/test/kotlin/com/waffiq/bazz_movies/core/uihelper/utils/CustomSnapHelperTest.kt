package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CustomSnapHelperTest {

  private lateinit var context: Context
  private lateinit var recyclerView: RecyclerView
  private lateinit var targetView: TextView
  private lateinit var layoutManager: LinearLayoutManager
  private val defaultOffset = -20

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext<Context>()
    recyclerView = RecyclerView(context)
    recyclerView.layoutManager = LinearLayoutManager(
      recyclerView.context, LinearLayoutManager.HORIZONTAL, false
    )
    layoutManager = recyclerView.layoutManager as LinearLayoutManager

    targetView = TextView(context)
    targetView.layoutParams = RecyclerView.LayoutParams(
      RecyclerView.LayoutParams.MATCH_PARENT,
      RecyclerView.LayoutParams.MATCH_PARENT
    )
    recyclerView.addView(targetView)
  }

  @Test
  fun calculateDistanceToFinalSnap_withDefaultOffset_updateOffsetCorrectly() {
    val snapHelper = CustomSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    // verify that the distances array is not null and has 2 elements (x and y distances)
    assertNotNull(distances)
    assertEquals(distances?.size, 2)

    // verify that the offset is applied to the x-distance
    val expectedXDistance = 0 - defaultOffset
    assertEquals(distances?.get(0), expectedXDistance)
  }

  @Test
  fun calculateDistanceToFinalSnap_withCustomOffsetZero_shouldUpdateDistanceToZero() {
    val snapHelper = CustomSnapHelper(0)
    snapHelper.attachToRecyclerView(recyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    assertNotNull(distances)
    assertEquals(distances?.size, 2)
    assertEquals(distances?.get(0), 0)
  }

  @Test
  fun calculateDistanceToFinalSnap_withCustomOffset90_shouldUpdateDistanceTo90() {
    val customOffset = 90
    val snapHelper = CustomSnapHelper(customOffset)
    snapHelper.attachToRecyclerView(recyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    assertNotNull(distances)
    assertEquals(distances?.size, 2)

    val expectedXDistance = 0 - customOffset
    assertEquals(distances?.get(0), expectedXDistance)
  }

  // edge case
  @Test
  fun calculateDistanceToFinalSnap_withLastItem_shouldUpdateDistanceCorrectly() {
    val lastItem = TextView(context).apply {
      layoutParams = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.MATCH_PARENT,
        RecyclerView.LayoutParams.MATCH_PARENT
      )
    }
    recyclerView.addView(lastItem)

    val snapHelper = CustomSnapHelper()
    snapHelper.attachToRecyclerView(recyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, lastItem)

    assertNotNull(distances)
    assertEquals(distances?.size, 2)
    val expectedXDistance = 0 - defaultOffset
    assertEquals(distances?.get(0), expectedXDistance)
  }

  @Test
  fun calculateDistanceToFinalSnap_withEmptyRecyclerView_returnNull() {
    val emptyRecyclerView = RecyclerView(context)
    emptyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    val snapHelper = CustomSnapHelper()
    snapHelper.attachToRecyclerView(emptyRecyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(emptyRecyclerView.layoutManager!!, targetView)

    assertNull(distances) // no view to snap to, should return null
  }

  @Test
  fun calculateDistanceToFinalSnap_withNullDistances_returnNull() {
    // Mock the helper to return null from getSuperDistances
    val snapHelper = mockk<CustomSnapHelper>(relaxed = true)
    every { snapHelper.getSuperDistances(any(), any()) } returns null
    every { snapHelper.calculateDistanceToFinalSnap(any(), any()) } answers {
      callOriginal()
    }

    val result = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    assertNull(result)
    verify { snapHelper.getSuperDistances(layoutManager, targetView) }
  }

  @Test
  fun calculateDistanceToFinalSnap_withVerticalLayoutManager_shouldUpdateDistanceCorrectly() {
    // mock the helper to return false from canLayoutManagerScrollHorizontally
    val snapHelper = mockk<CustomSnapHelper>(relaxed = true)
    every { snapHelper.getSuperDistances(any(), any()) } returns intArrayOf(10, 20)
    every { snapHelper.canLayoutManagerScrollHorizontally(any()) } returns false
    every { snapHelper.calculateDistanceToFinalSnap(any(), any()) } answers {
      callOriginal()
    }

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    assertNotNull(distances)
    assertEquals(2, distances?.size)
    // Verify the offset was NOT applied
    assertEquals(10, distances?.get(0))
    assertEquals(20, distances?.get(1))

    verify {
      snapHelper.getSuperDistances(layoutManager, targetView)
      snapHelper.canLayoutManagerScrollHorizontally(layoutManager)
    }
  }
}
