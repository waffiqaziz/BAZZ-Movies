package com.waffiq.bazz_movies.feature.detail.ui.adapter

import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class GenreAdapterTest : BaseAdapterTest() {
  private val genreId = listOf(1, 2, 3, 4, 5)
  private var adapter = GenreAdapter()

  @Before
  fun setup() {
    super.baseSetup()
    recyclerView.adapter = adapter
  }

  @Test
  fun areContentsTheSame_whenSameId_returnsTrue() {
    val oldItem = genreId
    val newItem = genreId // same content

    val diffCallback = GenreAdapter.DiffCallback(oldItem, newItem)
    assertTrue(diffCallback.areContentsTheSame(0, 0))
  }  @Test

  fun areItemsTheSame_whenFilePathIsSame_returnsTrue() {
    val oldItem = genreId
    val newItem = genreId // same content

    val diffCallback = GenreAdapter.DiffCallback(oldItem, newItem)
    assertTrue(diffCallback.areItemsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = genreId
    val newItem = listOf(6, 7, 8, 9, 10) // different content

    val diffCallback = GenreAdapter.DiffCallback(oldItem, newItem)
    assertFalse(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areItemsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = genreId
    val newItem = listOf(6, 7, 8, 9, 10) // different content

    val diffCallback = GenreAdapter.DiffCallback(oldItem, newItem)
    assertFalse(diffCallback.areItemsTheSame(0, 0))
  }
}
