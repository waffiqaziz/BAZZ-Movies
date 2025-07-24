package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.navigation.INavigator
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class RecommendationAdapterTest : BaseAdapterTest() {

  @Mock
  lateinit var navigator: INavigator

  private lateinit var adapter: RecommendationAdapter
  private lateinit var inflater: LayoutInflater
  private lateinit var binding: ItemPosterBinding
  private lateinit var viewHolder: RecommendationAdapter.ViewHolder
  private lateinit var parent: FrameLayout

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    super.baseSetup()
    MockitoAnnotations.openMocks(this)
    adapter = RecommendationAdapter(navigator)
    recyclerView.adapter = adapter

    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
    binding = ItemPosterBinding.inflate(inflater, parent, false)
    viewHolder = adapter.ViewHolder(binding)
  }

  @Test
  fun submitData_withPagingData_updatesTheAdapter() = runTest {
    val pagingData = PagingData.from(
      listOf(
        MediaItem(id = 1, title = "Movie 1"),
        MediaItem(id = 2, title = "Movie 2")
      )
    )

    adapter.submitData(pagingData)
    assertEquals(2, adapter.itemCount)
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectMovieData() = runTest {
    val movieData = MediaItem(
      mediaType = "movie",
      id = 1,
      title = "Transformers",
      originalTitle = "Transformers",
      listGenreIds = listOf(12, 878, 28),
      releaseDate = "2007-06-27"
    )

    submitPagingAndWait(listOf(movieData))

    assertEquals("Transformers", binding.imgPoster.contentDescription)
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectTvData() = runTest {
    val tvData = MediaItem(
      mediaType = "tv",
      id = 12345,
      title = "Bleach",
      originalTitle = "BLEACH",
      listGenreIds = listOf(10759, 16, 10765),
      releaseDate = "2004-10-05"
    )

    submitPagingAndWait(listOf(tvData))

    assertEquals("Bleach", binding.imgPoster.contentDescription)
  }

  @Test
  fun onBindViewHolder_withNullData_doesNotCrash() = runTest {
    val fakePagingData = PagingData.empty<MediaItem>()
    adapter.submitData(fakePagingData)
    advanceUntilIdle()

    assertEquals(0, adapter.itemCount)

    if (adapter.itemCount > 0) adapter.onBindViewHolder(viewHolder, 0)
  }

  @Test
  fun onBindViewHolder_withValidData_bindsDataCorrectly() = runTest {
    val dummyData =
      listOf(MediaItem(id = 1, title = "Title", mediaType = "movie"))
    val pagingData = PagingData.from(dummyData)

    adapter.submitData(pagingData)
    advanceUntilIdle()

    assertEquals(1, adapter.itemCount)
    adapter.onBindViewHolder(viewHolder, 0)
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun movieSearchItem_whenClicked_opensMovieDetails() = runTest {
    submitPagingAndWait(
      listOf(
        MediaItem(
          id = 1,
          title = "Test Movie",
          name = "Test Name",
          overview = "Test Overview",
          mediaType = "movie"
        )
      )
    )
    binding.imgPoster.performClick()

    val expectedItem = MediaItem(
      id = 1,
      title = "Test Movie",
      overview = "Test Overview",
      name = "Test Name",
      mediaType = "movie"
    )

    verify(navigator).openDetails(eq(context), eq(expectedItem))
  }

  @Test
  fun submitData_wthEmptyPagingData_shouldNotCrash() = runTest {
    val emptyPagingData = PagingData.from(emptyList<MediaItem>())
    adapter.submitData(emptyPagingData)
    assertEquals(0, adapter.itemCount)
  }

  @Test
  fun submitData_withMultiplePagingDataItems_shouldUpdateItemCountCorrectly() = runTest {
    val listData = listOf(
      MediaItem(id = 1, name = "Movie 1"),
      MediaItem(id = 2, title = "Movie 2"),
      MediaItem(id = 3, originalTitle = "Movie 3"),
      MediaItem(id = 4, originalName = "Movie 4"),
      MediaItem(id = 5),
      MediaItem(id = 6, posterPath = "poster"),
      MediaItem(id = 7, posterPath = ""),
      MediaItem(id = 8, posterPath = " "),
    )

    submitPagingAndWait(listData)
    assertEquals(8, adapter.itemCount)
    adapter.submitData(PagingData.empty())
  }

  @Test
  fun imgPoster_whenContentDescriptionAvailable_usesCorrectValue() = runTest {
    // all null
    submitDataYearReleased(
      null,
      MediaItem(id = 1, name = null, originalTitle = null, originalName = null)
    )

    // name valid
    submitDataYearReleased(
      "name",
      MediaItem(id = 1, name = "name", posterPath = "poster")
    )

    // title valid
    submitDataYearReleased(
      "title",
      MediaItem(id = 1, title = "title", posterPath = "")
    )

    // original title valid
    submitDataYearReleased(
      "original title",
      MediaItem(id = 1, originalTitle = "original title", posterPath = " ")
    )

    // original name valid
    submitDataYearReleased(
      "original name",
      MediaItem(id = 1, originalName = "original name")
    )

    // all  valid
    submitDataYearReleased(
      "name",
      MediaItem(
        id = 1,
        name = "name",
        title = "title",
        originalTitle = "original title",
        originalName = "original name",
      )
    )
  }

  @Test
  fun diffCallback_whenItemsAreTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem = MediaItem(id = 1, mediaType = "movie")

    assertTrue(RecommendationAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_whenItemsAreTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID

    assertFalse(RecommendationAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
  }

  @Test
  fun diffCallback_whenContentsAreTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = MediaItem(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = MediaItem(id = 1, mediaType = "movie", title = "Movie 1")

    assertTrue(RecommendationAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_whenContentsAreTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = MediaItem(id = 1, mediaType = "movie")
    val newItem1 = MediaItem(id = 2, mediaType = "movie") // different ID
    val newItem2 = MediaItem(id = 1, mediaType = "tv") // different mediaType

    assertFalse(RecommendationAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
    assertFalse(RecommendationAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem2))
  }

  private fun submitPagingAndWait(item: List<MediaItem>) = runTest {
    adapter.submitData(PagingData.from(item))
    advanceUntilIdle()
    adapter.onBindViewHolder(viewHolder, 0)
  }

  private fun submitDataYearReleased(expected: String?, item: MediaItem) = runTest {
    submitPagingAndWait(listOf(item))
    assertEquals(expected, binding.imgPoster.contentDescription)
  }
}
