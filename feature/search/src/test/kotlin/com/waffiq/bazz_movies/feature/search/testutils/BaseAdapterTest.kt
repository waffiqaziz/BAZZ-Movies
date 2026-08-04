package com.waffiq.bazz_movies.feature.search.testutils

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

abstract class BaseAdapterTest {

  protected lateinit var context: Context
  protected lateinit var parent: FrameLayout
  protected lateinit var inflater: LayoutInflater

  protected val navigator: INavigator = mockk()

  @Mock
  protected lateinit var recyclerView: RecyclerView

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  open fun baseSetup() {
    MockitoAnnotations.openMocks(this)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)

    every { navigator.openList(any(), any()) } just Runs
    every { navigator.openPersonDetails(any(), any()) } just Runs
    every { navigator.openDetails(any(), any()) } just Runs
  }
}
