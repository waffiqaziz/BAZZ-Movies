package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.paging

import android.widget.FrameLayout
import androidx.paging.PagingData
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.id.container_result
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_end
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_start
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPagingWatchlistBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.BaseAdapterPagingTest
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_NAME
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE_FORMATTED
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WatchlistPagingAdapterTest : BaseAdapterPagingTest() {
  private lateinit var adapter: WatchlistPagingAdapter
  private lateinit var binding: ItemPagingWatchlistBinding
  private lateinit var viewHolder: WatchlistPagingAdapter.ViewHolder

  @Before
  fun setup() {
    super.setUp()
    adapter = WatchlistPagingAdapter(
      navigator,
      MOVIE_MEDIA_TYPE,
      onDelete,
      onAddToWatchlist,
    )
    binding = ItemPagingWatchlistBinding.inflate(inflater, null, false)
    viewHolder = adapter.ViewHolder(binding)
  }

  @Test
  fun submitData_withPagingData_updateAdapter() = runTest {
    adapter.submitData(pagingData)
    assertEquals(1, adapter.itemCount)
  }

  @Test
  fun onBindViewHolder_whenAllDataIsValid_bindsCorrectMovieData() = runTest {
    // all valid
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals("Test Movie Name", binding.tvTitle.text.toString())
    assertEquals("Adventure", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("10/10", binding.tvRating.text.toString())
    assertEquals("5.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenSeveralDataIsEmpty_bindsCorrectMovieData() = runTest {
    // title, releaseDate valid, posterPath empty, listGenre empty
    adapter.submitData(
      PagingData.from(
        listOf(
          MediaItem(
            mediaType = "movie",
            title = MOVIE_TITLE,
            releaseDate = TEST_DATE,
            firstAirDate = null,
            listGenreIds = emptyList(),
            posterPath = ""
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals(MOVIE_TITLE, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenOriginalTitleIsValid_bindsCorrectMovieData() = runTest {
    // originalTitle valid, posterPath null
    adapter.submitData(
      PagingData.from(
        listOf(
          MediaItem(
            mediaType = "movie",
            originalTitle = MOVIE_ORIGINAL_TITLE,
            releaseDate = null,
            firstAirDate = TEST_DATE,
            posterPath = null
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals(MOVIE_ORIGINAL_TITLE, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenOriginalNameIsValid_bindsCorrectMovieData() = runTest {
    // originalName valid, date invalid
    adapter.submitData(
      PagingData.from(
        listOf(
          MediaItem(
            mediaType = "movie",
            originalName = MOVIE_ORIGINAL_NAME,
            releaseDate = null,
            firstAirDate = "invalid date",
            listGenreIds = null,
            voteAverage = null,
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals(MOVIE_ORIGINAL_NAME, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("N/A", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_allAttributeIsNull_bindsCorrectMovieData() = runTest {
    // all null
    adapter.submitData(PagingData.from(listOf(MediaItem())))
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals("N/A", binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("N/A", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onClick_whenClicked_opensMovieDetails() = runTest {
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.containerResult.performClick()

    verify { navigator.openDetails(eq(context), eq(movieData)) }
  }

  @Test
  fun bind_whenSwipedRight_callsOnDelete() = runTest {
    provideRecyclerView(adapter) { adapter.submitData(pagingData) }
    val viewHolder = provideViewHolder<WatchlistPagingAdapter.ViewHolder>()

    assertNotNull(viewHolder)
    assertNotNull(viewHolder!!.swipeCallback)

    // Trigger the actual callback
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(reveal_layout_start),
      100
    )

    verify { onDelete(movieData, 0) }
  }

  @Test
  fun bind_whenSwipedLeft_callsOnAddToWatchlist() = runTest {
    provideRecyclerView(adapter) { adapter.submitData(pagingData) }
    val viewHolder = provideViewHolder<WatchlistPagingAdapter.ViewHolder>()

    assertNotNull(viewHolder)
    assertNotNull(viewHolder!!.swipeCallback)

    // Trigger the actual callback
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(reveal_layout_end),
      100
    )

    verify { onAddToWatchlist(movieData, 0) }
  }

  @Test
  fun bind_whenSwiped_othersState() = runTest {
    provideRecyclerView(adapter) { adapter.submitData(pagingData) }
    val viewHolder = provideViewHolder<WatchlistPagingAdapter.ViewHolder>()
    assertNotNull(viewHolder)

    assertNotNull(viewHolder!!.swipeCallback)

    // simulate dragging
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_DRAGGING,
      viewHolder.itemView.findViewById(reveal_layout_end),
      100
    )

    // simulate swipe wrong item
    viewHolder.swipeCallback.onSwipeStateChanged(
      SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION,
      viewHolder.itemView.findViewById(container_result),
      100
    )
  }
}
