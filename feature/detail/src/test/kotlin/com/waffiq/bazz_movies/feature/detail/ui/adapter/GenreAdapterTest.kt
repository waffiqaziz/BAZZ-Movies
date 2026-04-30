package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.os.Looper
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf

class GenreAdapterTest : BaseAdapterTest() {

  private val movieGenreIds = listOf(28, 12)
  private val tvGenreIds = listOf(10759)

  private lateinit var adapter: GenreAdapter
  private lateinit var binding: ChipGenreBinding

  @Before
  fun setup() {
    super.baseSetup()
    adapter = GenreAdapter(navigator)
    recyclerView.adapter = adapter
    binding = ChipGenreBinding.inflate(inflater, null, false)
  }

  @Test
  fun setProviders_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(35)
    val newList = movieGenreIds

    adapter.setGenre(oldList)
    assertEquals(1, adapter.itemCount)

    adapter.setGenre(newList)
    assertEquals(2, adapter.itemCount)
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setGenre(movieGenreIds)
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(getGenreName(movieGenreIds.first()), binding.chip.text)
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
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setGenre(movieGenreIds)
    adapter.onBindViewHolder(viewHolder, 0)
    performClick()

    verify(exactly = 1) {
      navigator.openList(
        any(),
        ListArgs(
          listType = ListType.BY_GENRE,
          mediaType = MOVIE_MEDIA_TYPE,
          title = "",
          id = movieGenreIds.first(),
        ),
      )
    }

    // set media type
    adapter.setGenre(tvGenreIds)
    adapter.onBindViewHolder(viewHolder, 0)
    adapter.setMediaType(TV_MEDIA_TYPE)
    performClick()

    verify(exactly = 1) {
      navigator.openList(
        any(),
        ListArgs(
          listType = ListType.BY_GENRE,
          mediaType = TV_MEDIA_TYPE,
          title = "",
          id = tvGenreIds.first(),
        ),
      )
    }
  }

  private fun performClick() {
    binding.chip.performClick()

    // wait UI
    shadowOf(Looper.getMainLooper()).idle()
  }
}
