package com.waffiq.bazz_movies.feature.home.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.home.ui.HomeFragment
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.AsianViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Rule

abstract class BaseHomeFragmentTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

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

  protected fun createSuccessThenErrorFlow(
    items: List<MediaItem> = listOf(mediaItem, mediaItem.copy(id = 2)),
  ): Flow<PagingData<MediaItem>> =
    Pager(
      config = PagingConfig(pageSize = 10, enablePlaceholders = false),
      pagingSourceFactory = { SuccessThenErrorPagingSource(items) },
    ).flow

  protected fun createSuccessThenStuckFlow(
    items: List<MediaItem> = listOf(mediaItem, mediaItem.copy(id = 2)),
  ): Flow<PagingData<MediaItem>> =
    Pager(
      config = PagingConfig(pageSize = 10, enablePlaceholders = false),
      pagingSourceFactory = { SuccessThenStuckPagingSource(items) },
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

  protected fun setupMockViewModel(
    movieViewModel: MovieViewModel,
    tvSeriesViewModel: TvSeriesViewModel,
  ) {
    every { movieViewModel.getTopRatedMovies() } returns createPagingFlow()
    every { movieViewModel.getPopularMovies() } returns createPagingFlow()
    every { movieViewModel.trendingPeriod } returns trendingPeriodFlow
    every { movieViewModel.trending } returns createPagingFlow()
    every { movieViewModel.getUpcomingMovies() } returns createPagingFlow()
    every { movieViewModel.getPlayingNowMovies() } returns createPagingFlow()
    every { movieViewModel.setTrendingPeriod(any()) } just Runs
    every { tvSeriesViewModel.getPopularTv() } returns createPagingFlow()
    every { tvSeriesViewModel.getAiringThisWeekTv() } returns createPagingFlow()
    every { tvSeriesViewModel.getAiringTodayTv() } returns createPagingFlow()
    every { tvSeriesViewModel.getTopRatedTv() } returns createPagingFlow()
  }

  private val animePeriodFlow = MutableStateFlow(AnimePeriod.THIS_SEASON)

  protected fun setupMockAnimeViewModel(asianViewModel: AsianViewModel) {
    every { asianViewModel.animePeriod } returns animePeriodFlow
    every { asianViewModel.anime } returns createPagingFlow()
    every { asianViewModel.getAsianRomance() } returns createPagingFlow()
    every { asianViewModel.getCostumeDrama() } returns createPagingFlow()
    every { asianViewModel.getDonghua() } returns createPagingFlow()
  }

  protected fun setupMockNavigator(navigator: INavigator) {
    every { navigator.openList(any(), any()) } just Runs
  }

  protected fun setupMockRegion(
    userPreferenceViewModel: UserPreferenceViewModel,
    regionViewModel: RegionViewModel,
    region: String = "US",
  ) {
    every { userPreferenceViewModel.getUserRegionPref() } returns MutableLiveData(region)
    every { userPreferenceViewModel.saveRegionPref(any()) } just Runs
    every { regionViewModel.getCountryCode() } just Runs
    every { regionViewModel.countryCode } returns MutableLiveData("US")
  }

  protected fun launchFragment() {
    val result = launchFragmentInHiltContainer<HomeFragment>()
    scenario = result.scenario
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
  }

  protected fun verifyOpenList(mockNavigator: INavigator, listArgs: ListArgs) {
    verify { mockNavigator.openList(any(), listArgs) }
  }

  @After
  fun tearDown() {
    scenario.close()
    clearAllMocks()
  }
}
