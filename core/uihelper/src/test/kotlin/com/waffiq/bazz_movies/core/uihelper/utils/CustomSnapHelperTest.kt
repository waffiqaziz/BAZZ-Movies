package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
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
  private val defaultOffset = -50

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
  fun `test calculateDistanceToFinalSnap with default offset`() {
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
  fun `test calculateDistanceToFinalSnap with custom offset 0`() {
    val snapHelper = CustomSnapHelper(0)
    snapHelper.attachToRecyclerView(recyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(layoutManager, targetView)

    assertNotNull(distances)
    assertEquals(distances?.size, 2)
    assertEquals(distances?.get(0), 0)
  }

  @Test
  fun `test calculateDistanceToFinalSnap with custom offset 90`() {
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
  fun `test calculateDistanceToFinalSnap with last item`() {
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
  fun `test calculateDistanceToFinalSnap with empty RecyclerView`() {
    val emptyRecyclerView = RecyclerView(context)
    emptyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    val snapHelper = CustomSnapHelper()
    snapHelper.attachToRecyclerView(emptyRecyclerView)

    val distances = snapHelper.calculateDistanceToFinalSnap(emptyRecyclerView.layoutManager!!, targetView)

    assertNull(distances) // no view to snap to, should return null
  }
}
