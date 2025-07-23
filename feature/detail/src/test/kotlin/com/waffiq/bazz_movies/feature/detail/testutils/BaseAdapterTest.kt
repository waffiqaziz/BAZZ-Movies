package com.waffiq.bazz_movies.feature.detail.testutils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseAdapterTest {

  protected lateinit var context: Context

  @Mock
  protected lateinit var recyclerView: RecyclerView

  @Before
  open fun baseSetup() {
    MockitoAnnotations.openMocks(this)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    recyclerView = mock(RecyclerView::class.java)
  }
}