package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemMulmedBinding
import com.waffiq.bazz_movies.core.domain.ResultItem
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

@RunWith(RobolectricTestRunner::class)
class FavoriteTvAdapterTest {
  private lateinit var context: Context
  private lateinit var navigator: INavigator
  private lateinit var adapter: FavoriteTvAdapter
  private lateinit var inflater: LayoutInflater
  private lateinit var binding: ItemMulmedBinding
  private lateinit var viewHolder: FavoriteTvAdapter.ViewHolder

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val tvTitle = "Test Tv Title"
  private val tvOriginalTitle = "Test Tv Original Title"
  private val tvOriginalName = "Test Tv Original Name"

  private val tvData = ResultItem(
    id = 1,
    title = tvTitle,
    overview = "Test Overview",
    name = "Test Name",
    mediaType = "tv",
    releaseDate = "2004-10-05",
    listGenreIds = listOf(12, 878, 28)
  )

  @Before
  fun setup() {
    navigator = mockk(relaxed = true)
    adapter = FavoriteTvAdapter(navigator)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    inflater = LayoutInflater.from(context)
    binding = ItemMulmedBinding.inflate(inflater, null, false)
    viewHolder = adapter.ViewHolder(binding)
  }

  @Test
  fun submitData_withPagingData_updatesTheAdapter() = runTest {
    val pagingData = PagingData.from(
      listOf(tvData)
    )

    adapter.submitData(pagingData)
    assertEquals(1, adapter.itemCount)
  }

  @Test
  fun onBindViewHolder_allDataIsValid_bindsCorrectMovieData() = runTest {
    // test case 1: all valid
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "tv",
            name = "Test Tv Name",
            title = tvTitle,
            originalTitle = tvOriginalTitle,
            originalName = tvOriginalName,
            firstAirDate = TEST_DATE,
            releaseDate = TEST_DATE,
            listGenreIds = listOf(12),
            voteAverage = 10f,
            posterPath = "posterPath.jpg"
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("Test Tv Name", binding.tvTitle.text.toString())
    assertEquals("Adventure", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("10/10", binding.tvRating.text.toString())
    assertEquals("5.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenSeveralDataIsEmpty_bindsCorrectMovieData() = runTest {
    // test case 2: title, releaseDate valid, posterPath empty, listGenre empty
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "tv",
            title = tvTitle,
            releaseDate = TEST_DATE,
            firstAirDate = null,
            listGenreIds = emptyList(),
            posterPath = ""
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(tvTitle, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenOriginalTitleIsValid_bindsCorrectMovieData() = runTest {
    // test case 3: originalTitle valid, posterPath null
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "tv",
            originalTitle = tvOriginalTitle,
            releaseDate = null,
            firstAirDate = TEST_DATE,
            posterPath = null
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(tvOriginalTitle, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals(TEST_DATE_FORMATTED, binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenOriginalNameIsValid_bindsCorrectMovieData() = runTest {
    // test case 4: originalName valid, date invalid
    adapter.submitData(
      PagingData.from(
        listOf(
          ResultItem(
            mediaType = "tv",
            originalName = tvOriginalName,
            releaseDate = null,
            firstAirDate = "invalid date",
            listGenreIds = null,
            voteAverage = null,
          )
        )
      )
    )
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(tvOriginalName, binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())
    assertEquals("N/A", binding.tvYearReleased.text.toString())
    assertEquals("0/10", binding.tvRating.text.toString())
    assertEquals("0.0", binding.ratingBar.rating.toString())
  }

  @Test
  fun onBindViewHolder_whenAllDataIsNull_bindsCorrectMovieData() = runTest {
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
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onClick_whenClicked_opensTvDetails() = runTest {
    val pagingData = PagingData.from(listOf(tvData))
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.container.performClick()

    verify { navigator.openDetails(eq(context), eq(tvData)) }
  }

  @Test
  fun areItemsTheSame_whenIdAndMediaTypeIsSame_returnsTrue() {
    val oldItem = ResultItem(id = 1, mediaType = "tv")
    val newItem = ResultItem(id = 1, mediaType = "tv")

    assertTrue(FavoriteTvAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = ResultItem(id = 1, mediaType = "tv")
    val newItem1 = ResultItem(id = 2, mediaType = "tv") // different ID

    assertFalse(FavoriteTvAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
  }

  @Test
  fun areContentsTheSame_whenIdAndMediaType_returnsTrue() {
    val oldItem = ResultItem(id = 1, mediaType = "tv", title = "Tv 1")
    val newItem = ResultItem(id = 1, mediaType = "tv", title = "Different Title")

    assertTrue(FavoriteTvAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferentId_returnsFalse() {
    val oldItem = ResultItem(id = 1, mediaType = "tv")
    val newItem1 = ResultItem(id = 2, mediaType = "tv") // different ID

    assertFalse(FavoriteTvAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
  }
}
