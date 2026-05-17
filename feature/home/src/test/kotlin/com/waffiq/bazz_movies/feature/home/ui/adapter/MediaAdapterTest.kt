package com.waffiq.bazz_movies.feature.home.ui.adapter

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.home.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.navigation.MediaSource
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: MediaAdapter
  private lateinit var binding: ItemPosterBinding
  private lateinit var viewHolder: MediaAdapter.ViewHolder

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  override fun setup() {
    super.setup()
    binding = ItemPosterBinding.inflate(inflater, parent, false)
  }

  @Test
  fun submitData_withPagingData_bindsCorrectMovieData() =
    runTest {
      setupTrendingAdapter()
      adapter.submitData(fakeMovieMediaItemPagingData)
      adapter.onBindViewHolder(viewHolder, 0)
      assertEquals(2, adapter.itemCount)

      assertEquals("Inception", binding.imgPoster.contentDescription)
    }

  @Test
  fun onBindViewHolder_withNullData_doesNotCrash() =
    runTest {
      setupTrendingAdapter()
      val fakePagingData = PagingData.empty<MediaItem>()
      adapter.submitData(fakePagingData)
      advanceUntilIdle()

      assertEquals(0, adapter.itemCount)

      if (adapter.itemCount > 0) adapter.onBindViewHolder(viewHolder, 0)
    }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    setupTrendingAdapter()
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun trendingItem_whenClicked_opensDetailsWithCorrectArgs() =
    runTest {
      setupTrendingAdapter()
      submitPagingAndWait(mediaMovieItem)

      binding.imgPoster.performClick()
      verify(navigator).openDetails(context, mediaMovieItem)
    }

  @Test
  fun movieItem_whenClicked_opensDetailsWithCorrectArgs() =
    runTest {
      adapter = MediaAdapter(navigator, MediaSource.Typed(MOVIE_MEDIA_TYPE))
      setupViewHolderAndSubmit()

      binding.imgPoster.performClick()
      verify(navigator).openDetails(context, mediaMovieItem)
    }

  @Test
  fun tvItem_whenClicked_opensDetailsWithCorrectArgs() =
    runTest {
      adapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
      setupViewHolderAndSubmit()

      binding.imgPoster.performClick()
      verify(navigator).openDetails(context, mediaMovieItem.copy(mediaType = TV_MEDIA_TYPE))
    }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem

    assertTrue(MediaAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
    assertTrue(MediaAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem.copy(id = 2)

    assertFalse(MediaAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
    assertFalse(MediaAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  private fun setupTrendingAdapter() {
    adapter = MediaAdapter(navigator, MediaSource.Trending)
    recyclerView.adapter = adapter
    viewHolder = adapter.ViewHolder(binding)
  }

  private fun setupViewHolderAndSubmit() {
    recyclerView.adapter = adapter
    viewHolder = adapter.ViewHolder(binding)
    submitPagingAndWait(mediaMovieItem)
  }

  private fun submitPagingAndWait(item: MediaItem) =
    runTest {
      adapter.submitData(PagingData.from(listOf(item)))
      advanceUntilIdle()
      adapter.onBindViewHolder(viewHolder, 0)
    }
}
