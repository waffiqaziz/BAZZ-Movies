package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import org.robolectric.RobolectricTestRunner

import android.content.Context
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
abstract class BaseAdapterTest<T, A : RecyclerView.Adapter<*>> {

  protected lateinit var context: Context
  protected lateinit var recyclerView: RecyclerView
  protected lateinit var navigator: INavigator

  @Before
  open fun setUp() {
    navigator = mockk(relaxed = true)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
  }

  protected fun provideRecyclerView(
    adapter: A,
    submitData: suspend () -> Unit
  ) = runTest {
    recyclerView = RecyclerView(context)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    submitData()
    advanceUntilIdle()

    recyclerView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY)
    )
    recyclerView.layout(0, 0, 1080, 1920)
    shadowOf(Looper.getMainLooper()).idle()
  }

  protected inline fun <reified VH : RecyclerView.ViewHolder> provideViewHolder(): VH? =
    recyclerView.findViewHolderForAdapterPosition(0) as? VH
}
