package com.waffiq.bazz_movies.feature.search.ui.adapter

import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SearchDiffCallBackTest {

  @Test
  fun diffCallback_whenItemsAreTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = MultiSearchItem(id = 1, mediaType = "movie")
    val newItem = MultiSearchItem(id = 1, mediaType = "movie")

    assertTrue(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_whenItemsAreTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = MultiSearchItem(id = 1, mediaType = "movie")
    val newItem1 = MultiSearchItem(id = 2, mediaType = "movie") // different ID
    val newItem2 = MultiSearchItem(id = 1, mediaType = "tv") // different mediaType

    assertFalse(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
    assertFalse(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem2))
  }

  @Test
  fun diffCallback_whenContentsAreTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = MultiSearchItem(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = MultiSearchItem(id = 1, mediaType = "movie", title = "Different Title")

    assertTrue(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_whenContentsAreTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = MultiSearchItem(id = 1, mediaType = "movie")
    val newItem1 = MultiSearchItem(id = 2, mediaType = "movie") // different ID
    val newItem2 = MultiSearchItem(id = 1, mediaType = "tv") // different mediaType

    assertFalse(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
    assertFalse(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem2))
  }
}
