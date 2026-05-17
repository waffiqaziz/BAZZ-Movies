package com.waffiq.bazz_movies.feature.home.testutils

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

abstract class BaseFragmentHelperTest {

  protected lateinit var lifecycleOwner: TestLifecycleOwner

  protected lateinit var recyclerView: RecyclerView

  protected lateinit var context: Context
  protected lateinit var inflater: LayoutInflater
  protected lateinit var parentLayout: FrameLayout

  protected val adapter = mockk<PagingDataAdapter<String, RecyclerView.ViewHolder>>(relaxed = true)

  @Before
  open fun setup() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    lifecycleOwner = TestLifecycleOwner(
      initialState = Lifecycle.State.RESUMED,
      coroutineDispatcher = UnconfinedTestDispatcher(),
    )
    inflater = LayoutInflater.from(context)
    parentLayout = FrameLayout(context)

    recyclerView = RecyclerView(context)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  protected fun emptyLoadStates() =
    buildCombinedLoadStates(
      refresh = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = true),
    )

  protected fun nonEmptyLoadStates() =
    buildCombinedLoadStates(
      refresh = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
    )

  protected fun buildCombinedLoadStates(
    refresh: LoadState = LoadState.NotLoading(false),
    prepend: LoadState = LoadState.NotLoading(false),
    append: LoadState = LoadState.NotLoading(false),
  ) = CombinedLoadStates(
    refresh = refresh,
    prepend = prepend,
    append = append,
    source = LoadStates(
      refresh = refresh,
      prepend = prepend,
      append = append,
    ),
    mediator = null,
  )
}
