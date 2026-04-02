package com.waffiq.bazz_movies.feature.list.testutils

import android.content.Context
import android.content.Intent
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakePagingMediaItem
import com.waffiq.bazz_movies.feature.list.ui.ListActivity
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class BaseListActivityTest {

  protected val listResultsFlow: Flow<PagingData<MediaItem>> = flowOf(fakePagingMediaItem)
  protected val movieGenreArgs = ListArgs(
    listType = ListType.BY_GENRE,
    mediaType = MOVIE_MEDIA_TYPE,
    title = "title",
    genreId = 878,
  )
  protected val tvGenreArgs = movieGenreArgs.copy(mediaType = TV_MEDIA_TYPE)
  protected val movieKeywordsArgs = ListArgs(
    listType = ListType.BY_KEYWORD,
    mediaType = MOVIE_MEDIA_TYPE,
    title = "post apocalyptic",
    genreId = 134,
  )
  protected val tvKeywordsArgs = movieKeywordsArgs.copy(mediaType = TV_MEDIA_TYPE)
  protected val movieNowPlayingArgs = ListArgs(
    listType = ListType.NOW_PLAYING,
    mediaType = MOVIE_MEDIA_TYPE,
    title = "",
  )
  protected val tvNowPlayingArgs = movieNowPlayingArgs.copy(mediaType = TV_MEDIA_TYPE)
  protected val moviePopularArgs = movieNowPlayingArgs.copy(listType = ListType.POPULAR)
  protected val movieTopRatedArgs = movieNowPlayingArgs.copy(listType = ListType.TOP_RATED)
  protected val movieUpcomingArgs = movieNowPlayingArgs.copy(listType = ListType.UPCOMING)
  protected val tvPopularArgs = movieNowPlayingArgs.copy(
    listType = ListType.POPULAR, mediaType = TV_MEDIA_TYPE
  )
  protected val tvTopRatedArgs = movieNowPlayingArgs.copy(
    listType = ListType.TOP_RATED, mediaType = TV_MEDIA_TYPE
  )
  protected val tvAiringThisWeekArgs = movieNowPlayingArgs.copy(
    listType = ListType.AIRING_THIS_WEEK, mediaType = TV_MEDIA_TYPE
  )


  protected fun setupMock(viewModel: ListViewModel, navigator: INavigator) {
    every { viewModel.getMovieByGenres(any()) } returns listResultsFlow
    every { viewModel.getTvByGenres(any()) } returns listResultsFlow
    every { viewModel.getMovieByKeywords(any()) } returns listResultsFlow
    every { viewModel.getTvByKeywords(any()) } returns listResultsFlow
    every { navigator.openDetails(any(), any()) } just Runs
  }

  protected fun Context.launchListActivity(
    block: (ActivityScenario<ListActivity>) -> Unit,
  ) {
    this.launchListActivity(movieGenreArgs) { block(it) }
  }

  protected fun Context.launchNullListActivity(
    args: ListArgs?,
    block: (ActivityScenario<ListActivity>) -> Unit,
  ) {
    val intent = Intent(this, ListActivity::class.java).apply {
      putExtra(EXTRA_LIST, args)
    }

    ActivityScenario.launch<ListActivity>(intent).use { scenario ->
      block(scenario)
    }
  }

  protected open fun Context.launchListActivity(
    args: ListArgs,
    block: (ActivityScenario<ListActivity>) -> Unit,
  ) {
    val intent = Intent(this, ListActivity::class.java).apply {
      putExtra(EXTRA_LIST, args) // match production exactly
    }

    ActivityScenario.launch<ListActivity>(intent).use { scenario ->
      scenario.onActivity { /* do nothing */ }
      block(scenario)
    }
  }

  protected fun shouldShowTv(){
    onView(withText("TV"))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  protected fun shouldShowMovie(){
    onView(withText("MOVIE"))
      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }
}
