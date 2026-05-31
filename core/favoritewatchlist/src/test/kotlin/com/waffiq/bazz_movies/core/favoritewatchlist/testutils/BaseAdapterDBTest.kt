package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.models.Favorite
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseAdapterDBTest : BaseAdapterTest<Favorite, RecyclerView.Adapter<*>>() {

  protected lateinit var onDelete: (Favorite, Int) -> Unit
  protected lateinit var onAddToWatchlist: (Favorite, Int) -> Unit

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  override fun setUp() {
    super.setUp()
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)
  }
}
