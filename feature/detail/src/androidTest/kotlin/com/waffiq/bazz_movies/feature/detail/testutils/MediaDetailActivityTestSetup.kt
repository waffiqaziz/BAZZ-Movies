package com.waffiq.bazz_movies.feature.detail.testutils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Interface for setting up the test environment for [MediaDetailActivity].
 *
 * This interface defines the necessary properties and methods to prepare the test environment,
 * including mock data, view models, and navigation.
 */
interface MediaDetailActivityTestSetup {

  val uiState: MutableStateFlow<MediaDetailUiState>
  val errorEvent: MutableSharedFlow<String>
  val toastEvent: MutableSharedFlow<Int>
  val recommendations: MutableStateFlow<PagingData<MediaItem>>
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

  fun Context.launchMediaDetailActivity(block: (ActivityScenario<MediaDetailActivity>) -> Unit)

  fun Context.launchMediaDetailActivity(
    data: MediaItem,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  )

  fun Context.launchNullMediaDetailActivity(
    data: MediaItem? = null,
    block: (ActivityScenario<MediaDetailActivity>) -> Unit,
  )

  fun checkIntentData(link: String)

  fun updateState(block: MediaDetailUiState.() -> MediaDetailUiState) =
    uiState.update { it.block() }
}
