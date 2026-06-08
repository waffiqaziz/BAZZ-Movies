package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterPagingTest
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.movieData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WatchlistPagingAdapterTest : BaseAdapterPagingTest() {

  // this test only run to cover the SwipeConfig.forWatchlist() case

  @Before
  fun setup() {
    super.setUp()
    adapter = MediaPagingAdapter(
      navigator,
      MOVIE_MEDIA_TYPE,
      MediaPagingAdapter.SwipeConfig.forWatchlist(),
      onDelete,
      onAddToWatchlist,
    )
  }

  @Test
  fun submitData_withPagingData_updateAdapter() =
    runTest {
      val pagingData = PagingData.from(listOf(movieData.copy(voteAverage = null)))
      viewHolder = adapter.ViewHolder(binding)

      adapter.submitData(pagingData)
      adapter.onBindViewHolder(viewHolder, 0)
      assertEquals(1, adapter.itemCount)
    }
}
