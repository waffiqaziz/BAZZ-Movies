package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging.MediaAdapterPagingHelper.DIFF_CALLBACK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DiffCallbackPagingTest {

  @Test
  fun areItemsTheSame_whenIdAndMediaTypeIsSame_returnsTrue() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem = MediaItem(id = 1, mediaType = "movie")

    assertTrue(DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID

    assertFalse(DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
  }

  @Test
  fun areContentsTheSame_whenIdAndMediaTypeIsSame_returnsTrue() {
    val oldItem = MediaItem(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = MediaItem(id = 1, mediaType = "movie", title = "Different Title")

    assertTrue(DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID

    assertFalse(DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
  }
}
