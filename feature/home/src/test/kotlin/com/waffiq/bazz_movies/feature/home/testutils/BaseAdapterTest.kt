package com.waffiq.bazz_movies.feature.home.testutils

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.navigation.INavigator
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

abstract class BaseAdapterTest {

  val mediaMovieItem = MediaItem(
    id = 1,
    title = "Inception",
    overview = "A mind-bending thriller",
    posterPath = "/poster1.jpg",
    mediaType = "movie",
    voteAverage = 8.8f,
    releaseDate = "2010-07-16",
  )

  val fakeMovieMediaItemPagingData =
    PagingData.from(
      listOf(
        mediaMovieItem,
        mediaMovieItem.copy(id = 2),
      ),
    )

  lateinit var context: Context
  lateinit var inflater: LayoutInflater
  lateinit var parent: FrameLayout

  @Mock
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  @Mock
  lateinit var observer: RecyclerView.AdapterDataObserver

  @Before
  open fun setup() {
    MockitoAnnotations.openMocks(this)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
  }
}
