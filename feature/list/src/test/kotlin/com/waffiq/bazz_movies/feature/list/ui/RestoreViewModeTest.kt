package com.waffiq.bazz_movies.feature.list.ui

import com.waffiq.bazz_movies.feature.list.domain.model.ListViewMode
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.restoreViewMode
import org.junit.Assert.assertEquals
import org.junit.Test

class RestoreViewModeTest {

  @Test
  fun restore_null_returnsDefault() {
    assertEquals(ListViewMode.TWO_COLUMNS, restoreViewMode(null))
  }

  @Test
  fun restore_unknownName_returnsDefault() {
    assertEquals(ListViewMode.TWO_COLUMNS, restoreViewMode("GARBAGE"))
  }

  @Test
  fun restore_everySavedName_restoresCorrectly() {
    ListViewMode.entries.forEach {
      assertEquals(it, restoreViewMode(it.name))
    }
  }
}
