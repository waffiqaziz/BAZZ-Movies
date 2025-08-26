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
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaState
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testOMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testTvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testUserModel
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testWatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Test helper for [MediaDetailActivity] that provides mock implementations
 * of [MediaDetailActivityTestSetup] and sets up necessary LiveData and ViewModel mocks
 */
class MediaDetailActivityTestHelper : MediaDetailActivityTestSetup {

  override val isFavorite = MutableLiveData<Boolean>()
  override val isWatchlist = MutableLiveData<Boolean>()
  override val itemState = MutableLiveData<MediaState>()
  override val mediaCredits = MutableLiveData<MediaCredits>()
  override val omdbResult = MutableLiveData<OMDbDetails>()
  override val loadingState = MutableLiveData<Boolean>()
  override val errorState = MutableSharedFlow<String>(replay = 1)
  override val rateState = MutableLiveData<Event<Boolean>>()
  override val postModelState = MutableLiveData<Event<PostModelState>>()
  override val linkVideo = MutableLiveData<String>()
  override val detailMedia = MutableLiveData<MediaDetail>()
  override val tvExternalID = MutableLiveData<TvExternalIds>()
  override val recommendation = MutableLiveData<PagingData<MediaItem>>()
  override val watchProvidersUiState = MutableLiveData<WatchProvidersUiState>()
  override lateinit var context: Context

  override val token = MutableLiveData<String>()
  override val region = MutableLiveData<String>()
  override val userModel = MutableLiveData<UserModel>()

  override fun setupBaseMocks() {
    isFavorite.postValue(false)
    isWatchlist.postValue(false)
    itemState.postValue(testMediaState)
    mediaCredits.postValue(testMediaCredits)
    omdbResult.postValue(testOMDbDetails)
    loadingState.postValue(false)

    linkVideo.postValue("link video")
    detailMedia.postValue(testMediaDetail)
    tvExternalID.postValue(testTvExternalIds)
    recommendation.postValue(PagingData.from(listOf(testMediaItem, testMediaItem)))
    watchProvidersUiState.postValue(testWatchProvidersUiState)

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
    every { mockMediaDetailViewModel.getTvExternalIds(any()) } just Runs
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
    every { mockMediaDetailViewModel.isFavorite } returns isFavorite
    every { mockMediaDetailViewModel.isWatchlist } returns isWatchlist
    every { mockMediaDetailViewModel.itemState } returns itemState
    every { mockMediaDetailViewModel.mediaCredits } returns mediaCredits
    every { mockMediaDetailViewModel.omdbResult } returns omdbResult
    every { mockMediaDetailViewModel.loadingState } returns loadingState
    every { mockMediaDetailViewModel.errorState } returns errorState
    every { mockMediaDetailViewModel.rateState } returns rateState
    every { mockMediaDetailViewModel.postModelState } returns postModelState
    every { mockMediaDetailViewModel.linkVideo } returns linkVideo
    every { mockMediaDetailViewModel.detailMedia } returns detailMedia
    every { mockMediaDetailViewModel.tvExternalID } returns tvExternalID
    every { mockMediaDetailViewModel.recommendation } returns recommendation
    every { mockMediaDetailViewModel.watchProvidersUiState } returns watchProvidersUiState
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
    this.launchMediaDetailActivity(testMediaItem, block)
  }

  override fun Context.launchMediaDetailActivity(
    data: MediaItem,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, MediaDetailActivity::class.java).apply {
      putExtra(MediaDetailActivity.EXTRA_MOVIE, data)
    }

    ActivityScenario.launch<MediaDetailActivity>(intent).use { scenario ->
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
