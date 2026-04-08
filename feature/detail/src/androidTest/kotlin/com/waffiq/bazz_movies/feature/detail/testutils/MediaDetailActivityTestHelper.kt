package com.waffiq.bazz_movies.feature.detail.testutils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testUserModel
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testWatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Test helper for [MediaDetailActivity] that provides mock implementations
 * of [MediaDetailActivityTestSetup] and sets up necessary LiveData and ViewModel mocks
 */
class MediaDetailActivityTestHelper : MediaDetailActivityTestSetup {

  override val recommendations = MutableStateFlow<PagingData<MediaItem>>(
    value = PagingData.empty()
  )
  override val errorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
  override val toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
  override val uiState = MutableStateFlow(MediaDetailUiState())
  override lateinit var context: Context

  override val token = MutableLiveData<String>()
  override val region = MutableLiveData<String>()
  override val userModel = MutableLiveData<UserModel>()

  val sRecommendations = recommendations.asStateFlow()
  val sErrorEvent = errorEvent.asSharedFlow()
  val sToastEvent = toastEvent.asSharedFlow()
  val sUiState = uiState.asStateFlow()

  override fun setupBaseMocks() {

    uiState.value = MediaDetailUiState(
      detail = testMediaDetail,
      credits = MediaCredits(
        cast = listOf(MediaCastItem()),
        id = 90,
        crew = listOf(MediaCrewItem())
      ),
      omdbDetails = OMDbDetails(),
      videoLink = "link",
      recommendations = PagingData.from(listOf(MediaItem())),
      watchProviders = testWatchProvidersUiState,
      itemState = MediaState(
        id = 90,
        favorite = false,
        rated = Rated.Value(90.0),
        watchlist = false
      ),
      isFavorite = false,
      isWatchlist = false,
      mediaStateResult = null, // it should only initiate when add to watchlist/favorite
      isLoading = false,
    )

    token.postValue("NaN")
    region.postValue("id")
    userModel.postValue(testUserModel)
  }

  override fun setupMediaDetailViewModelMocks(mockMediaDetailViewModel: MediaDetailViewModel) {
    every { mockMediaDetailViewModel.getMovieVideoLink(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieDetail(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieCredits(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieRecommendation(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieState(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieWatchProviders(any()) } just Runs
    every { mockMediaDetailViewModel.getTvTrailerLink(any()) } just Runs
    every { mockMediaDetailViewModel.getTvDetail(any()) } just Runs
    every { mockMediaDetailViewModel.getTvCredits(any()) } just Runs
    every { mockMediaDetailViewModel.getTvRecommendation(any()) } just Runs
    every { mockMediaDetailViewModel.getTvState(any()) } just Runs
    every { mockMediaDetailViewModel.getTvWatchProviders(any()) } just Runs
    every { mockMediaDetailViewModel.getOMDbDetails(any()) } just Runs
    every { mockMediaDetailViewModel.handleBtnFavorite(any(), any(), any()) } just Runs
    every { mockMediaDetailViewModel.handleBtnWatchlist(any(), any(), any()) } just Runs
    every { mockMediaDetailViewModel.isFavoriteDB(any(), any()) } just Runs
    every { mockMediaDetailViewModel.isWatchlistDB(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postFavorite(any()) } just Runs
    every { mockMediaDetailViewModel.postWatchlist(any()) } just Runs
    every { mockMediaDetailViewModel.postMovieRate(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postTvRate(any(), any()) } just Runs
  }

  override fun setupPreferencesViewModelMocks(mockPrefViewModel: DetailUserPrefViewModel) {
    every { mockPrefViewModel.getUserToken() } returns token
  }

  override fun setupObservables(mockMediaDetailViewModel: MediaDetailViewModel) {
    every { mockMediaDetailViewModel.uiState } returns sUiState
    every { mockMediaDetailViewModel.errorEvent } returns sErrorEvent
    every { mockMediaDetailViewModel.toastEvent } returns sToastEvent
    every { mockMediaDetailViewModel.recommendations } returns sRecommendations
  }

  override fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs
  }

  override fun initializeTest(context: Context) {
    this.context = context
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      Glide.get(context).clearMemory()
    }
  }

  override fun Context.launchMediaDetailActivity(
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    this.launchMediaDetailActivity(testMediaItem) { block(it) }
  }

  override fun Context.launchMediaDetailActivity(
    data: MediaItem,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, MediaDetailActivity::class.java).apply {
      putExtra(MediaDetailActivity.EXTRA_MOVIE, data)
    }

    ActivityScenario.launch<MediaDetailActivity>(intent).use { scenario ->
      scenario.onActivity { /* do nothing */ }
      block(scenario)
    }
  }

  override fun Context.launchNullMediaDetailActivity(
    data: MediaItem?,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, MediaDetailActivity::class.java).apply {
      data?.let {
        putExtra(MediaDetailActivity.EXTRA_MOVIE, it)
      }
    }

    ActivityScenario.launch<MediaDetailActivity>(intent).use { scenario ->
      block(scenario)
    }
  }

  override fun checkIntentData(link: String) {
    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(link.toUri()))
  }


}
