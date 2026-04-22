package com.waffiq.bazz_movies.feature.list.utils

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.feature.list.utils.RecyclerViewLayoutHelper.restoreInstanceState
import com.waffiq.bazz_movies.feature.list.utils.RecyclerViewLayoutHelper.saveInstanceState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class RecyclerViewLayoutHelperTest {

  private val recyclerView = mockk<RecyclerView>(relaxed = true)
  private val layoutManager = mockk<RecyclerView.LayoutManager>(relaxed = true)
  private val state = mockk<Parcelable>()

  @Test
  fun saveInstanceState_whenCalled_returnsStateCorrectly() {
    every { recyclerView.layoutManager } returns layoutManager
    every { layoutManager.onSaveInstanceState() } returns state

    val result = recyclerView.saveInstanceState

    assertNotNull(result)
    verify { layoutManager.onSaveInstanceState() }
  }

  @Test
  fun saveInstanceState_withNullLayoutManager_returnsError() {
    every { recyclerView.layoutManager } returns null

    assertFailsWith<IllegalArgumentException> {
      recyclerView.saveInstanceState
    }
  }


  @Test
  fun restoreInstanceState_whenCalled_restoreStateCorrectly() {
    every { recyclerView.layoutManager } returns layoutManager

    recyclerView.restoreInstanceState(state)

    verify { layoutManager.onRestoreInstanceState(any()) }
  }

  @Test
  fun restoreInstanceState_withNullLayoutManager_runsWithoutError() {
    every { recyclerView.layoutManager } returns null
    recyclerView.restoreInstanceState(state)
  }
}
