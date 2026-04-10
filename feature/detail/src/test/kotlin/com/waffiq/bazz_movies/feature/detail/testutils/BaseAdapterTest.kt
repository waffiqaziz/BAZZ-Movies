package com.waffiq.bazz_movies.feature.detail.testutils

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseAdapterTest {

  protected lateinit var context: Context
  protected lateinit var parent: FrameLayout
  protected lateinit var inflater: LayoutInflater

  @Mock
  protected lateinit var recyclerView: RecyclerView

  protected val navigator: INavigator = mockk()

  @Before
  open fun baseSetup() {
    MockitoAnnotations.openMocks(this)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    recyclerView = mock(RecyclerView::class.java)
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)

    every { navigator.openList(any(), any()) } just Runs
    every { navigator.openPersonDetails(any(), any()) } just Runs
    every { navigator.openDetails(any(), any()) } just Runs
  }
}
