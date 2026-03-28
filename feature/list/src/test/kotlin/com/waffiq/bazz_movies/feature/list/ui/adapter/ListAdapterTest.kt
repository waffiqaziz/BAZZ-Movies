package com.waffiq.bazz_movies.feature.list.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemListBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem2
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaTvResponseItem
import com.waffiq.bazz_movies.navigation.INavigator
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
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ListAdapterTest {
  lateinit var context: Context

  @Mock
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  private lateinit var adapter: ListAdapter
  private lateinit var inflater: LayoutInflater
  private lateinit var binding: ItemListBinding
  private lateinit var viewHolder: ListAdapter.ViewHolder
  private lateinit var parent: FrameLayout

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)
    adapter = ListAdapter(navigator)
    recyclerView.adapter = adapter
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
    binding = ItemListBinding.inflate(inflater, parent, false)
    viewHolder = adapter.ViewHolder(binding)
  }

  @Test
  fun submitData_withPagingData_bindsCorrectMovieData() = runTest {
    adapter.submitData(fakeMovieMediaItemPagingData)
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(2, adapter.itemCount)

    // set media type
    adapter.setMediaType("tv")

    // movie
    submitPagingAndWait(mediaMovieResponseItem.toMediaItem())
    assertEquals("Inception", binding.imgPoster.contentDescription.toString())

    // tv
    submitPagingAndWait(mediaTvResponseItem.toMediaItem())
    assertEquals("Breaking Bad", binding.imgPoster.contentDescription.toString())

    // null & empty poster
    submitPagingAndWait(mediaMovieResponseItem.toMediaItem().copy(posterPath = null))
    submitPagingAndWait(mediaMovieResponseItem.toMediaItem().copy(posterPath = ""))
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
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun movieSearchItem_whenClicked_opensMovieDetails() = runTest {
    submitPagingAndWait(mediaMovieResponseItem.toMediaItem())
    binding.imgPoster.performClick()

    val expectedItem = mediaMovieResponseItem.toMediaItem()
    verify(navigator).openDetails(context, expectedItem)
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = mediaMovieResponseItem.toMediaItem()
    val newItem = mediaMovieResponseItem.toMediaItem()

    assertTrue(ListAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = mediaMovieResponseItem.toMediaItem()
    val newItem = mediaMovieResponseItem2.toMediaItem()

    assertFalse(ListAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  private fun submitPagingAndWait(item: MediaItem) = runTest {
    adapter.submitData(PagingData.from(listOf(item)))
    advanceUntilIdle()
    adapter.onBindViewHolder(viewHolder, 0)
  }
}