package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.core.domain.ResultItem
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

@RunWith(RobolectricTestRunner::class)
class FavoriteMovieAdapterTest {
  private lateinit var context: Context
  private lateinit var navigator: INavigator
  private lateinit var adapter: FavoriteMovieAdapter

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val movieData = ResultItem(
    id = 1,
    title = "Test Movie",
    overview = "Test Overview",
    name = "Test Name",
    mediaType = "movie",
    releaseDate = "2004-10-05",
    listGenreIds = listOf(12, 878, 28)
  )

  @Before
  fun setup() {
    navigator = mockk(relaxed = true)
    adapter = FavoriteMovieAdapter(navigator)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
  }

  @Test
  fun submitData_withPagingData_updateAdapter() = runTest {
    val pagingData = PagingData.from(
      listOf(movieData)
    )

    adapter.submitData(pagingData)
    assertEquals(1, adapter.itemCount)
  }

  @Test
  fun onBindViewHolder_bindCorrectMovieData_allPossibility() = runTest {
    val inflater = LayoutInflater.from(context)
    val binding = ItemMulmedBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    // test case 1: all valid
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "movie",
            name = "Test Movie Name",
            title = "Test Movie Title",
            originalTitle = "Test Movie Original Title",
            originalName = "Test Movie Original Name",
            firstAirDate = "2007-06-27",
            releaseDate = "2007-06-27",
            listGenreIds = listOf(12),
            voteAverage = 10f,
            posterPath = "posterPath.jpg"
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("Test Movie Name", binding.tvTitle.text.toString())
    assertEquals("Adventure", binding.tvGenre.text.toString())
    assertEquals("Jun 27, 2007", binding.tvYearReleased.text.toString())
    assertEquals("10/10", binding.tvRating.text.toString())
    assertEquals("5.0", binding.ratingBar.rating.toString())

    // test case 2: title, releaseDate valid, posterPath empty, listGenre empty
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "movie",
            title = "Test Movie Title",
            releaseDate = "2007-06-27",
            firstAirDate = null,
            listGenreIds = emptyList(),
            posterPath = ""
          )
        )
      )
    )

    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("Test Movie Title", binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("Jun 27, 2007", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())

    // test case 3: originalTitle valid, posterPath null
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "movie",
            originalTitle = "Test Movie Original Title",
            releaseDate = null,
            firstAirDate = "2007-06-27",
            posterPath = null
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("Test Movie Original Title", binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("Jun 27, 2007", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())

    // test case 4: originalName valid, date invalid
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "movie",
            originalName = "Test Movie Original Name",
            releaseDate = null,
            firstAirDate = "invalid date",
            listGenreIds = null,
            voteAverage = null,
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("Test Movie Original Name", binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("N/A", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())

    // test case 5: all null
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem()
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
  fun onCreateViewHolder_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onClick_openDetailsMovie() = runTest {
    val inflater = LayoutInflater.from(context)
    val binding = ItemMulmedBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(movieData))
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.container.performClick()


    verify { navigator.openDetails(eq(context), eq(movieData)) }
  }

  @Test
  fun diffCallback_areItemsTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = ResultItem(id = 1, mediaType = "movie")
    val newItem = ResultItem(id = 1, mediaType = "movie")

    assertTrue(FavoriteMovieAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_areItemsTheSame_returnsFalseForDifferentId() {
    val oldItem = ResultItem(id = 1, mediaType = "movie")
    val newItem1 = ResultItem(id = 2, mediaType = "movie") // different ID

    assertFalse(FavoriteMovieAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
  }

  @Test
  fun diffCallback_areContentsTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = ResultItem(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = ResultItem(id = 1, mediaType = "movie", title = "Different Title")

    assertTrue(FavoriteMovieAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_areContentsTheSame_returnsFalseForDifferentId() {
    val oldItem = ResultItem(id = 1, mediaType = "movie")
    val newItem1 = ResultItem(id = 2, mediaType = "movie") // different ID

    assertFalse(FavoriteMovieAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
  }
}
