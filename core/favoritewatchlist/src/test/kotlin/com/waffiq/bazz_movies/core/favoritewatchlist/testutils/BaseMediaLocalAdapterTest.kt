package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.designsystem.databinding.ListItemMediaSwipeBinding
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local.MediaLocalAdapter
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SwipeConfig
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseMediaLocalAdapterTest : BaseAdapterTest<Favorite, RecyclerView.Adapter<*>>() {

  protected lateinit var onDelete: (Favorite, Int) -> Unit
  protected lateinit var onAddToWatchlist: (Favorite, Int) -> Unit
  protected lateinit var binding: ListItemMediaSwipeBinding
  protected lateinit var adapter: MediaLocalAdapter
  protected lateinit var viewHolder: MediaLocalAdapter.ViewHolder

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  override fun setUp() {
    super.setUp()
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)

    binding = ListItemMediaSwipeBinding.inflate(inflater, null, false)

    adapter = MediaLocalAdapter(navigator, SwipeConfig.forFavorite(), onDelete, onAddToWatchlist)
    viewHolder = adapter.ViewHolder(binding)
  }
}
