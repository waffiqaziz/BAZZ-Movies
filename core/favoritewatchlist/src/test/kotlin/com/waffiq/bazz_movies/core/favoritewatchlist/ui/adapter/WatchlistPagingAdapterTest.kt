package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.listitem.SwipeableListItem
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.id.container_result
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_end
import com.waffiq.bazz_movies.core.designsystem.R.id.reveal_layout_start
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPagingWatchlistBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_NAME
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE_FORMATTED
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class WatchlistPagingAdapterTest {
  private lateinit var context: Context
  private lateinit var navigator: INavigator
  private lateinit var adapter: WatchlistPagingAdapter
  private lateinit var inflater: LayoutInflater
  private lateinit var binding: ItemPagingWatchlistBinding
  private lateinit var viewHolder: WatchlistPagingAdapter.ViewHolder
  private lateinit var onDelete: (MediaItem, Int) -> Unit
  private lateinit var onAddToWatchlist: (MediaItem, Int) -> Unit

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val movieData = MediaItem(
    mediaType = "movie",
    name = "Test Movie Name",
    title = MOVIE_TITLE,
    originalTitle = MOVIE_ORIGINAL_TITLE,
    originalName = MOVIE_ORIGINAL_NAME,
    firstAirDate = TEST_DATE,
    releaseDate = TEST_DATE,
    listGenreIds = listOf(12),
    voteAverage = 10f,
    posterPath = "posterPath.jpg"
  )
  private val pagingData = PagingData.from(listOf(movieData))

  @Before
  fun setup() {
    navigator = mockk(relaxed = true)
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)
    adapter = WatchlistPagingAdapter(
      navigator,
      MOVIE_MEDIA_TYPE,
      onDelete,
      onAddToWatchlist,
    )
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    inflater = LayoutInflater.from(context)
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
    adapter.submitData(
      PagingData.from(
        listOf(
          MediaItem()
        )
      )
    )
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
    val pagingData = PagingData.from(listOf(movieData))
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.containerResult.performClick()

    verify { navigator.openDetails(eq(context), eq(movieData)) }
  }

  @Test
  fun bind_whenSwipedRight_callsOnDelete() = runTest {
    val recyclerView = RecyclerView(context)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    adapter.submitData(pagingData)
    advanceUntilIdle()

    // measure the RecyclerView
    recyclerView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY)
    )
    recyclerView.layout(0, 0, 1080, 1920)
    shadowOf(Looper.getMainLooper()).idle()

    val viewHolder =
      recyclerView.findViewHolderForAdapterPosition(0) as? WatchlistPagingAdapter.ViewHolder
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
    val recyclerView = RecyclerView(context)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    adapter.submitData(pagingData)
    advanceUntilIdle()

    // measure the RecyclerView
    recyclerView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY)
    )
    recyclerView.layout(0, 0, 1080, 1920)
    shadowOf(Looper.getMainLooper()).idle()

    val viewHolder =
      recyclerView.findViewHolderForAdapterPosition(0) as? WatchlistPagingAdapter.ViewHolder
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
    val recyclerView = RecyclerView(context)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    adapter.submitData(pagingData)
    advanceUntilIdle()

    // measure the RecyclerView
    recyclerView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY)
    )
    recyclerView.layout(0, 0, 1080, 1920)
    shadowOf(Looper.getMainLooper()).idle()

    val viewHolder =
      recyclerView.findViewHolderForAdapterPosition(0) as? WatchlistPagingAdapter.ViewHolder
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

  @Test
  fun areItemsTheSame_whenIdAndMediaTypeIsSame_returnsTrue() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem = MediaItem(id = 1, mediaType = "movie")

    assertTrue(WatchlistPagingAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID

    assertFalse(WatchlistPagingAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
  }

  @Test
  fun areContentsTheSame_whenIdAndMediaTypeIsSame_returnsTrue() {
    val oldItem = MediaItem(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = MediaItem(id = 1, mediaType = "movie", title = "Different Title")

    assertTrue(WatchlistPagingAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID

    assertFalse(WatchlistPagingAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
  }
}
