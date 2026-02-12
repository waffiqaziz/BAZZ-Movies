package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import android.view.LayoutInflater
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_NAME
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseAdapterPagingTest : BaseAdapterTest<MediaItem, PagingDataAdapter<MediaItem, *>>() {

  protected lateinit var onDelete: (MediaItem, Int) -> Unit
  protected lateinit var onAddToWatchlist: (MediaItem, Int) -> Unit
  protected lateinit var inflater: LayoutInflater

  protected val movieData = MediaItem(
    mediaType = "movie",
    name = "Test Movie Name",
    title = MOVIE_TITLE,
    originalTitle = MOVIE_ORIGINAL_TITLE,
    originalName = MOVIE_ORIGINAL_NAME,
    firstAirDate = TEST_DATE,
    releaseDate = TEST_DATE,
    listGenreIds = listOf(12),
    voteAverage = 10f,
    posterPath = "posterPath.jpg"
  )
  protected val pagingData = PagingData.from(listOf(movieData))

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  override fun setUp() {
    super.setUp()
    onDelete = mockk(relaxed = true)
    onAddToWatchlist = mockk(relaxed = true)
    inflater = LayoutInflater.from(context)
  }
}
