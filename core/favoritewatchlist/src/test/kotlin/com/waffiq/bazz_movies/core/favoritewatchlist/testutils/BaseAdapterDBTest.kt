package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.domain.Favorite
import io.mockk.mockk
import org.junit.Before

abstract class BaseAdapterDBTest : BaseAdapterTest<Favorite, RecyclerView.Adapter<*>>() {

  protected lateinit var onDelete: (Favorite, Int) -> Unit
  protected lateinit var onAddToWatchlist: (Favorite, Int) -> Unit

  @Before
  override fun setUp() {
    super.setUp()
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)
  }
}
