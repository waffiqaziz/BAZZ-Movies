package com.waffiq.bazz_movies.feature.list.testutils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.trending
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.model.media.MediaItem
import com.waffiq.bazz_movies.feature.list.R.id.btn_toggle_layout
import com.waffiq.bazz_movies.feature.list.R.id.rv_list
import com.waffiq.bazz_movies.feature.list.domain.model.ListViewMode
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakePagingMediaItem
import com.waffiq.bazz_movies.feature.list.ui.ListActivity
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.feature.list.ui.ViewModeBottomSheet
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import io.mockk.every
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import javax.inject.Inject

abstract class BaseListActivityTest {

  protected lateinit var context: Context
  protected lateinit var intent: Intent

  @Inject
  lateinit var mockNavigator: INavigator

  private val tvArgs = ListArgs(
    listType = ListType.BY_GENRE,
    mediaType = MediaSource.Typed(TV_MEDIA_TYPE),
    title = "",
  )

  private val listResultsFlow: Flow<PagingData<MediaItem>> = flowOf(fakePagingMediaItem)
  protected val movieGenreArgs = ListArgs(
    listType = ListType.BY_GENRE,
    mediaType = MediaSource.Typed(MOVIE_MEDIA_TYPE),
    title = "title",
    id = 878,
  )
  protected val tvGenreArgs = movieGenreArgs.copy(mediaType = MediaSource.Typed(TV_MEDIA_TYPE))
  protected val movieKeywordsArgs = ListArgs(
    listType = ListType.BY_KEYWORD,
    mediaType = MediaSource.Typed(MOVIE_MEDIA_TYPE),
    title = "post apocalyptic",
    id = 134,
  )
  protected val tvKeywordsArgs =
    movieKeywordsArgs.copy(mediaType = MediaSource.Typed(TV_MEDIA_TYPE))
  protected val movieNowPlayingArgs = ListArgs(
    listType = ListType.NOW_PLAYING,
    mediaType = MediaSource.Typed(MOVIE_MEDIA_TYPE),
    title = "",
  )
  protected val tvNowPlayingArgs =
    movieNowPlayingArgs.copy(mediaType = MediaSource.Typed(TV_MEDIA_TYPE))
  protected val moviePopularArgs = movieNowPlayingArgs.copy(listType = ListType.POPULAR)
  protected val movieTopRatedArgs = movieNowPlayingArgs.copy(listType = ListType.TOP_RATED)
  protected val movieUpcomingArgs = movieNowPlayingArgs.copy(listType = ListType.UPCOMING)
  protected val movieRecommendationArgs = movieNowPlayingArgs.copy(
    listType = ListType.RECOMMENDATION,
    title = "Movie Title",
    id = 12344,
  )
  protected val trendingTodayArgs = movieNowPlayingArgs.copy(
    listType = ListType.TRENDING_TODAY,
    mediaType = MediaSource.Trending,
    title = "",
  )
  protected val trendingThisWeekArgs = movieNowPlayingArgs.copy(
    listType = ListType.TRENDING_WEEK,
    mediaType = MediaSource.Trending,
    title = "",
  )
  protected val tvPopularArgs = tvArgs.copy(listType = ListType.POPULAR)
  protected val tvTopRatedArgs = tvArgs.copy(listType = ListType.TOP_RATED)
  protected val tvAiringThisWeekArgs = tvArgs.copy(listType = ListType.AIRING_THIS_WEEK)
  protected val tvRecommendationArgs = tvArgs.copy(
    listType = ListType.RECOMMENDATION,
    title = "Tv Title",
    id = 12344,
  )
  protected val animeAllTimeArgs = tvArgs.copy(listType = ListType.ANIME_ALL_TIME)
  protected val animeThisSeasonArgs = tvArgs.copy(listType = ListType.ANIME_THIS_SEASON)
  protected val costumeDramaArgs = tvArgs.copy(listType = ListType.COSTUME_DRAMA)
  protected val donghuaArgs = tvArgs.copy(listType = ListType.DONGHUA)
  protected val romanceDramaArgs = tvArgs.copy(listType = ListType.ROMANCE_DRAMA)
  protected val realityShow = tvArgs.copy(listType = ListType.REALITY_SHOW)

  @Before
  open fun setup() {
    Intents.init()
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    intent = Intent(context, ListActivity::class.java).apply {
      putExtra(EXTRA_LIST, movieGenreArgs)
    }
  }

  protected fun setupMock(viewModel: ListViewModel) {
    every { viewModel.getAiringThisWeekTv() } returns listResultsFlow
    every { viewModel.getByGenre(any(), any()) } returns listResultsFlow
    every { viewModel.getByKeyword(any(), any()) } returns listResultsFlow
    every { viewModel.getNowPlaying(any()) } returns listResultsFlow
    every { viewModel.getRecommendation(any(), any()) } returns listResultsFlow
    every { viewModel.getTopRated(any()) } returns listResultsFlow
    every { viewModel.getUpcomingMovies() } returns listResultsFlow
    every { viewModel.getPopular(any()) } returns listResultsFlow
  }

  protected fun Context.launchListActivity(block: (ActivityScenario<ListActivity>) -> Unit) {
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

  protected fun Context.launchListActivityAndRecreate(
    args: ListArgs = movieGenreArgs,
    block: (ActivityScenario<ListActivity>) -> Unit,
  ) {
    val intent = Intent(this, ListActivity::class.java).apply {
      putExtra(EXTRA_LIST, args)
    }

    ActivityScenario.launch<ListActivity>(intent).use { scenario ->
      scenario.recreate()
      block(scenario)
    }
  }

  protected fun ActivityScenario<ListActivity>.sendEmptyViewModeResult() {
    onActivity { activity ->
      activity.supportFragmentManager.setFragmentResult(
        ViewModeBottomSheet.REQUEST_KEY,
        Bundle(), // no RESULT_MODE key
      )
    }
  }

  protected fun ActivityScenario<ListActivity>.selectViewMode(mode: ListViewMode) {
    onActivity { activity ->
      activity.supportFragmentManager.setFragmentResult(
        ViewModeBottomSheet.REQUEST_KEY,
        Bundle().apply {
          putString(ViewModeBottomSheet.RESULT_MODE, mode.name)
        },
      )
    }
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }

  protected fun ActivityScenario<ListActivity>.assertLayout(mode: ListViewMode) {
    onActivity { activity ->
      val rv = activity.findViewById<RecyclerView>(rv_list)
      if (mode.isDetailed) {
        assertTrue(rv.layoutManager is LinearLayoutManager)
        assertFalse(rv.layoutManager is GridLayoutManager)
      } else {
        assertEquals(mode.spanCount, (rv.layoutManager as GridLayoutManager).spanCount)
      }
    }
  }

  protected fun shouldShowTv() {
    "TV".isVisible()
  }

  protected fun shouldShowTrending() {
    trending.isTextVisible()
  }

  protected fun shouldShowMovie() {
    "MOVIE".isVisible()
  }

  protected fun triggerListButton(id: Int) {
    btn_toggle_layout.isDisplayed()
    btn_toggle_layout.performClick()
    id.performClick()
  }
}
