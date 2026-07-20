package com.waffiq.bazz_movies.feature.home.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.testutils.pagingsource.NeverLoadingPagingSource
import com.waffiq.bazz_movies.feature.home.testutils.pagingsource.TestPagingSource
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import com.waffiq.bazz_movies.feature.home.ui.fragment.HomeFragment
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.AsianViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseHomeFragmentTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockSnackbar: ISnackbar

  @Inject
  lateinit var mockAsianViewModel: AsianViewModel

  @Inject
  lateinit var mockMovieViewModel: MovieViewModel

  @Inject
  lateinit var mockTvSeriesViewModel: TvSeriesViewModel

  @Inject
  lateinit var mockUserPreferenceViewModel: UserPreferenceViewModel

  @Inject
  lateinit var mockRegionViewModel: RegionViewModel

  lateinit var scenario: ActivityScenario<*>

  private val mediaItem = MediaItem(
    id = 1,
    title = "movie title",
    overview = "overview",
    posterPath = "/poster1.jpg",
    backdropPath = "/backdrop1.jpg",
    mediaType = "movie",
    voteAverage = 8.8f,
    releaseDate = "2016-07-16",
  )

  protected fun createPagingFlow(
    items: List<MediaItem> = listOf(mediaItem, mediaItem.copy(id = 2)),
  ): Flow<PagingData<MediaItem>> =
    Pager(
      config = PagingConfig(pageSize = 10, enablePlaceholders = false),
      pagingSourceFactory = { TestPagingSource(items) },
    ).flow

  protected fun createErrorPagingFlow(): Flow<PagingData<MediaItem>> =
    Pager(
      config = PagingConfig(pageSize = 10, enablePlaceholders = false),
      pagingSourceFactory = { TestPagingSource(emptyList(), shouldError = true) },
    ).flow

  protected fun createStuckLoadingFlow(): Flow<PagingData<MediaItem>> =
    Pager(
      config = PagingConfig(pageSize = 10, enablePlaceholders = false),
      pagingSourceFactory = { NeverLoadingPagingSource() },
    ).flow

  private val trendingPeriodFlow = MutableStateFlow(TrendingPeriod.WEEK)

  protected fun setupMockViewModel() {
    every { mockMovieViewModel.getTopRatedMovies() } returns createPagingFlow()
    every { mockMovieViewModel.getPopularMovies() } returns createPagingFlow()
    every { mockMovieViewModel.trendingPeriod } returns trendingPeriodFlow
    every { mockMovieViewModel.trending } returns createPagingFlow()
    every { mockMovieViewModel.getUpcomingMovies() } returns createPagingFlow()
    every { mockMovieViewModel.getPlayingNowMovies() } returns createPagingFlow()
    every { mockMovieViewModel.setTrendingPeriod(any()) } just Runs
    every { mockTvSeriesViewModel.getPopularTv() } returns createPagingFlow()
    every { mockTvSeriesViewModel.getAiringThisWeekTv() } returns createPagingFlow()
    every { mockTvSeriesViewModel.getAiringTodayTv() } returns createPagingFlow()
    every { mockTvSeriesViewModel.getTopRatedTv() } returns createPagingFlow()
  }

  private val animePeriodFlow = MutableStateFlow(AnimePeriod.THIS_SEASON)

  protected fun setupMockAnimeViewModel() {
    every { mockAsianViewModel.animePeriod } returns animePeriodFlow
    every { mockAsianViewModel.anime } returns createPagingFlow()
    every { mockAsianViewModel.getAsianRomance() } returns createPagingFlow()
    every { mockAsianViewModel.getCostumeDrama() } returns createPagingFlow()
    every { mockAsianViewModel.getDonghua() } returns createPagingFlow()
  }

  protected fun setupMockNavigator() {
    every { mockNavigator.openList(any(), any()) } just Runs
  }

  private fun setupMockRegion() {
    every { mockUserPreferenceViewModel.getUserRegionPref() } returns MutableLiveData("US")
    every { mockUserPreferenceViewModel.saveRegionPref(any()) } just Runs
    every { mockRegionViewModel.getCountryCode() } just Runs
    every { mockRegionViewModel.countryCode } returns MutableLiveData("US")
  }

  protected fun launchFragment() {
    val result = launchFragmentInHiltContainer<HomeFragment>()
    scenario = result.scenario
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }

  protected fun verifyOpenList(mockNavigator: INavigator, listArgs: ListArgs) {
    verify { mockNavigator.openList(any(), listArgs) }
  }

  protected fun tvArgs(listType: ListType) =
    ListArgs(listType, MediaSource.Typed(TV_MEDIA_TYPE), "")

  protected fun movieArgs(listType: ListType) =
    ListArgs(listType, MediaSource.Typed(MOVIE_MEDIA_TYPE), "")

  @Before
  open fun setup() {
    hiltRule.inject()
    setupMockNavigator()
    setupMockRegion()
    setupMockViewModel()
    setupMockAnimeViewModel()
  }

  @After
  fun tearDown() {
    scenario.close()
  }
}
