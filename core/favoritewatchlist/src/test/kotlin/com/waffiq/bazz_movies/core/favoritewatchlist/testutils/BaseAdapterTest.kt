package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
abstract class BaseAdapterTest<T, A : RecyclerView.Adapter<*>> {

  protected lateinit var context: Context
  protected lateinit var parent: FrameLayout
  protected lateinit var recyclerView: RecyclerView
  protected lateinit var navigator: INavigator
  protected lateinit var inflater: LayoutInflater

  @Before
  open fun setUp() {
    navigator = mockk(relaxed = true)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
  }

  protected fun provideRecyclerView(adapter: A, submitData: suspend () -> Unit) =
    runTest {
      recyclerView = RecyclerView(context)
      recyclerView.adapter = adapter
      recyclerView.layoutManager = LinearLayoutManager(context)

      submitData()
      advanceUntilIdle()

      recyclerView.measure(
        View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY),
      )
      recyclerView.layout(0, 0, 1080, 1920)
      shadowOf(Looper.getMainLooper()).idle()
    }

  protected inline fun <reified VH : RecyclerView.ViewHolder> provideViewHolder(): VH? =
    recyclerView.findViewHolderForAdapterPosition(0) as? VH

  /**
   * Attaches [adapter] to a real RecyclerView, lays it out, and returns it.
   * Use this when tests involve click listeners that rely on [bindingAdapterPosition].
   */
  protected fun attachAdapter(adapter: A): RecyclerView =
    RecyclerView(context).apply {
      layoutManager = LinearLayoutManager(context)
      this.adapter = adapter
      measure(
        View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY),
      )
      layout(0, 0, 1080, 1920)
      shadowOf(Looper.getMainLooper()).idle()
    }
}
