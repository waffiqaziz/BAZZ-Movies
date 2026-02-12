package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

  // Synchronous version for DB adapters
  protected fun provideRecyclerViewSync(
    adapter: RecyclerView.Adapter<*>,
    submitList: () -> Unit,
  ) {
    recyclerView = RecyclerView(context)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    submitList()

    recyclerView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY)
    )
    recyclerView.layout(0, 0, 1080, 1920)
  }
}
