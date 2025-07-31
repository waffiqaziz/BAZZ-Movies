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
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Interface for setting up the test environment for [MediaDetailActivity].
 *
 * This interface defines the necessary properties and methods to prepare the test environment,
 * including mock data, view models, and navigation.
 */
interface MediaDetailActivityTestSetup {
  val isFavorite: MutableLiveData<Boolean>
  val isWatchlist: MutableLiveData<Boolean>
  val itemState: MutableLiveData<MediaState>
  val mediaCredits: MutableLiveData<MediaCredits>
  val omdbResult: MutableLiveData<OMDbDetails>
  val loadingState: MutableLiveData<Boolean>
  val errorState: MutableSharedFlow<String>
  val rateState: MutableLiveData<Event<Boolean>>
  val postModelState: MutableLiveData<Event<PostModelState>>
  val linkVideo: MutableLiveData<String>
  val detailMedia: MutableLiveData<MediaDetail>
  val tvExternalID: MutableLiveData<TvExternalIds>
  val recommendation: MutableLiveData<PagingData<MediaItem>>
  val watchProvidersUiState: MutableLiveData<WatchProvidersUiState>
  var context: Context

  val token: MutableLiveData<String>
  val region: MutableLiveData<String>
  val userModel: MutableLiveData<UserModel>

  fun setupBaseMocks()
  fun setupMediaDetailViewModelMocks(mockMediaDetailViewModel: MediaDetailViewModel)
  fun setupPreferencesViewModelMocks(mockPrefViewModel: DetailUserPrefViewModel)
  fun setupObservables(mockMediaDetailViewModel: MediaDetailViewModel)
  fun setupNavigatorMocks(mockNavigator: INavigator)
  fun initializeTest(context: Context)

  fun Context.launchMediaDetailActivity(
    block: (ActivityScenario<MediaDetailActivity>) -> Unit
  )

  fun Context.launchMediaDetailActivity(
    data: MediaItem,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit
  )

  fun checkIntentData(link: String)
}
