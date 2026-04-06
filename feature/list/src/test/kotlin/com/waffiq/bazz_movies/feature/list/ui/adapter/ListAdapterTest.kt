package com.waffiq.bazz_movies.feature.list.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemListBinding
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieItem2
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.mediaMovieResponseItem
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ListAdapterTest {
  lateinit var context: Context

  @Mock
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  @Mock
  lateinit var observer: RecyclerView.AdapterDataObserver

  private lateinit var adapter: ListAdapter
  private lateinit var inflater: LayoutInflater
  private lateinit var itemListBinding: ItemListBinding
  private lateinit var itemResultBInding: ItemResultBinding
  private lateinit var gridViewHolder: ListAdapter.GridViewHolder
  private lateinit var listViewHolder: ListAdapter.ListViewHolder
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
    itemListBinding = ItemListBinding.inflate(inflater, parent, false)
    itemResultBInding = ItemResultBinding.inflate(inflater, parent, false)
    gridViewHolder = adapter.GridViewHolder(itemListBinding)
    listViewHolder = adapter.ListViewHolder(itemResultBInding)
  }

  @Test
  fun submitData_withPagingData_bindsCorrectMovieData() = runTest {
    adapter.submitData(fakeMovieMediaItemPagingData)
    adapter.onBindViewHolder(gridViewHolder, 0)
    assertEquals(2, adapter.itemCount)

    // set media type
    adapter.setMediaType("tv")

    // movie
    submitPagingAndWait(mediaMovieItem)
    assertEquals("Inception", itemListBinding.imgPoster.contentDescription.toString())

    // tv
    submitPagingAndWait(mediaTvResponseItem.toMediaItem())
    assertEquals("Breaking Bad", itemListBinding.imgPoster.contentDescription.toString())

    // null & empty poster
    submitPagingAndWait(mediaMovieItem.copy(posterPath = null))
    submitPagingAndWait(mediaMovieItem.copy(posterPath = ""))
  }

  @Test
  fun getItemViewType_returnsGrid_whenGridModeTrue() {
    setupGridLayout()
    assertEquals(ListAdapter.VIEW_TYPE_GRID, adapter.getItemViewType(0))
  }

  @Test
  fun getItemViewType_returnsList_whenGridModeFalse() {
    setupListLayout()
    assertEquals(ListAdapter.VIEW_TYPE_LIST, adapter.getItemViewType(0))
  }

  @Test
  fun setGridMode_withValidData_showsCorrectView() = runTest {
    // 1. Submit and wait for items to arrive
    submitPagingAndWait(mediaMovieItem)
    assertEquals(1, adapter.itemCount)

    adapter.setMediaType("movie")

    // 2. Switch to grid and verify view type
    setupGridLayout()
    assertTrue(adapter.isGridMode())
    assertEquals(ListAdapter.VIEW_TYPE_GRID, adapter.getItemViewType(0))

    // 3. Switch to list and verify view type
    setupListLayout()
    assertFalse(adapter.isGridMode())
    assertEquals(ListAdapter.VIEW_TYPE_LIST, adapter.getItemViewType(0))
  }

  @Test
  fun setGridMode_doesNotNotify_whenModeUnchanged() = runTest {
    submitPagingAndWait(mediaMovieItem)
    adapter.registerAdapterDataObserver(observer) // mock or spy observer

    setupGridLayout() // already true by default, then should no notification
    verify(observer, never()).onItemRangeChanged(anyInt(), anyInt(), isNull())
  }

  @Test
  fun setGridMode_notifiesRange_whenModeChanges() = runTest {
    submitPagingAndWait(mediaMovieItem)
    adapter.registerAdapterDataObserver(observer)

    setupListLayout()
    verify(observer).onItemRangeChanged(0, adapter.itemCount, null)
  }

  @Test
  fun onBindViewHolder_withNullData_doesNotCrash() = runTest {
    val fakePagingData = PagingData.empty<MediaItem>()
    adapter.submitData(fakePagingData)
    advanceUntilIdle()

    assertEquals(0, adapter.itemCount)

    if (adapter.itemCount > 0) adapter.onBindViewHolder(gridViewHolder, 0)
  }

  @Test
  fun onCreateViewHolder_returnsGridViewHolder_whenViewTypeGrid() {
    val holder = adapter.onCreateViewHolder(parent, ListAdapter.VIEW_TYPE_GRID)
    assertThat(holder).isInstanceOf(ListAdapter.GridViewHolder::class.java)
    assertTrue(holder is ListAdapter.GridViewHolder)
  }

  @Test
  fun onCreateViewHolder_returnsListViewHolder_whenViewTypeList() {
    val holder = adapter.onCreateViewHolder(parent, ListAdapter.VIEW_TYPE_LIST)
    assertThat(holder).isInstanceOf(ListAdapter.ListViewHolder::class.java)
    assertTrue(holder is ListAdapter.ListViewHolder)
  }

  @Test
  fun gridViewHolder_bind_callsNavigatorOnClick() = runTest {
    adapter.setMediaType("movie")
    val media = mediaMovieItem

    gridViewHolder.bind(media)
    itemListBinding.imgPoster.performClick()

    verify(navigator).openDetails(
      itemListBinding.imgPoster.context,
      media.copy(mediaType = "movie")
    )
  }

  @Test
  fun listViewHolder_bind_showsCorrectTitle() = runTest {
    adapter.setMediaType("movie")
    setupListLayout()

    listViewHolder.bind(mediaMovieItem)

    val expected = mediaMovieResponseItem.title
    assertEquals(expected, listViewHolder.binding.tvTitle.text.toString())
  }

  @Test
  fun listViewHolder_bind_callsNavigatorOnClick() = runTest {
    adapter.setMediaType("movie")
    listViewHolder.bind(mediaMovieItem)
    itemResultBInding.containerResult.performClick()

    verify(navigator).openDetails(
      any(),
      eq(mediaMovieItem.copy(mediaType = "movie"))
    )
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
    submitPagingAndWait(mediaMovieItem)
    itemListBinding.imgPoster.performClick()

    val expectedItem = mediaMovieItem
    verify(navigator).openDetails(context, expectedItem)
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem

    assertTrue(ListAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferent_returnsFalse() {
    val oldItem = mediaMovieItem
    val newItem = mediaMovieItem2

    assertFalse(ListAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  private fun submitPagingAndWait(item: MediaItem) = runTest {
    adapter.submitData(PagingData.from(listOf(item)))
    advanceUntilIdle()
    adapter.onBindViewHolder(gridViewHolder, 0)
  }

  @Test
  fun onBindViewHolder_withGridViewHolder_callsGridBind() = runTest {
    adapter.setMediaType("movie")
    setupGridLayout()
    submitPagingAndWait(mediaMovieItem)

    val holder = adapter.onCreateViewHolder(parent, ListAdapter.VIEW_TYPE_GRID)
    adapter.onBindViewHolder(holder, 0)

    // trigger click to proves bind() was called
    val gridHolder = holder as ListAdapter.GridViewHolder
    gridHolder.itemView.findViewById<View>(itemListBinding.imgPoster.id).performClick()

    verify(navigator).openDetails(any(), eq(mediaMovieItem.copy(mediaType = "movie")))
  }

  @Test
  fun onBindViewHolder_withListViewHolder_callsListBind() = runTest {
    adapter.setMediaType("movie")
    setupListLayout()
    submitPagingAndWait(mediaMovieItem)

    val holder = adapter.onCreateViewHolder(parent, ListAdapter.VIEW_TYPE_LIST)
    adapter.onBindViewHolder(holder, 0)

    // perform click to check if its list layout
    val listHolder = holder as ListAdapter.ListViewHolder
    listHolder.binding.containerResult.performClick()

    verify(navigator).openDetails(any(), eq(mediaMovieItem.copy(mediaType = "movie")))
  }

  private fun setupListLayout() {
    adapter.setGridMode(false)
  }

  private fun setupGridLayout() {
    adapter.setGridMode(true)
  }
}
