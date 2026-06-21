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
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.mediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testUserModel
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
import kotlinx.coroutines.flow.update

/**
 * Test helper for [MediaDetailActivity] that provides mock implementations
 */
abstract class BaseMediaDetailActivityTest {

  protected val recommendations = MutableStateFlow(
    value = PagingData.from(listOf(testMediaItem)),
  )
  protected val errorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
  protected val toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
  protected val uiState = MutableStateFlow(MediaDetailUiState())
  protected lateinit var context: Context

  protected val token = MutableLiveData<String>()
  protected val region = MutableLiveData<String>()
  protected val userModel = MutableLiveData<UserModel>()

  val sRecommendations = recommendations.asStateFlow()
  val sErrorEvent = errorEvent.asSharedFlow()
  val sToastEvent = toastEvent.asSharedFlow()
  val sUiState = uiState.asStateFlow()

  protected fun setupBaseMocks() {
    uiState.value = mediaDetailUiState
    token.postValue("NaN")
    region.postValue("id")
    userModel.postValue(testUserModel)
  }

  protected fun setupMediaDetailViewModelMocks(mockMediaDetailViewModel: MediaDetailViewModel) {
    every { mockMediaDetailViewModel.getMovieDetail(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieCredits(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieRecommendation(any()) } just Runs
    every { mockMediaDetailViewModel.getMovieState(any()) } just Runs
    every { mockMediaDetailViewModel.getTvDetail(any()) } just Runs
    every { mockMediaDetailViewModel.getTvCredits(any()) } just Runs
    every { mockMediaDetailViewModel.getTvRecommendation(any()) } just Runs
    every { mockMediaDetailViewModel.getTvState(any()) } just Runs
    every { mockMediaDetailViewModel.getOMDbDetails(any()) } just Runs
    every { mockMediaDetailViewModel.handleBtnFavorite(any()) } just Runs
    every { mockMediaDetailViewModel.handleBtnWatchlist(any()) } just Runs
    every { mockMediaDetailViewModel.getByMedia(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postFavorite(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postWatchlist(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postMovieRate(any(), any()) } just Runs
    every { mockMediaDetailViewModel.postTvRate(any(), any()) } just Runs
  }

  protected fun setupPreferencesViewModelMocks(mockPrefViewModel: DetailUserPrefViewModel) {
    every { mockPrefViewModel.getUserToken() } returns token
  }

  protected fun setupObservables(mockMediaDetailViewModel: MediaDetailViewModel) {
    every { mockMediaDetailViewModel.uiState } returns sUiState
    every { mockMediaDetailViewModel.errorEvent } returns sErrorEvent
    every { mockMediaDetailViewModel.toastEvent } returns sToastEvent
    every { mockMediaDetailViewModel.recommendations } returns sRecommendations
  }

  protected fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs
    every { mockNavigator.openList(any(), any()) } just Runs
  }

  protected fun initializeTest(context: Context) {
    this.context = context
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      Glide.get(context).clearMemory()
    }
  }

  protected fun Context.launchMediaDetailActivity(
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    this.launchMediaDetailActivity(testMediaItem) { block(it) }
  }

  protected fun Context.launchMediaDetailActivity(
    data: MediaItem,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, MediaDetailActivity::class.java).apply {
      putExtra(MediaDetailActivity.EXTRA_MOVIE, data)
    }

    ActivityScenario.launch<MediaDetailActivity>(intent).use { scenario ->
      scenario.onActivity { /* do nothing */ }
      shortDelay()
      block(scenario)
    }
  }

  protected fun Context.launchNullMediaDetailActivity(
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

  protected fun checkIntentData(link: String) {
    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(link.toUri()))
  }

  protected fun updateState(block: MediaDetailUiState.() -> MediaDetailUiState) =
    uiState.update { it.block() }
}
