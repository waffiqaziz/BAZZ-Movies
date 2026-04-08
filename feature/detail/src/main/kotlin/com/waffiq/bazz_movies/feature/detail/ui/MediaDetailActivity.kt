package com.waffiq.bazz_movies.feature.detail.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarUtils.scrollActionBarBehavior
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityMediaDetailBinding
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailDataManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailUIManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.UserInteractionHandler
import com.waffiq.bazz_movies.feature.detail.ui.manager.WatchProvidersManager
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ParcelableHelper.extractMediaItemFromIntent
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MediaDetailActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityMediaDetailBinding
  private lateinit var dataExtra: MediaItem

  private val detailViewModel: MediaDetailViewModel by viewModels()
  private val prefViewModel: DetailUserPrefViewModel by viewModels()

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  lateinit var uiManager: DetailUIManager

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  lateinit var userInteractionHandler: UserInteractionHandler

  private lateinit var watchProvidersManager: WatchProvidersManager
  private lateinit var dataManager: DetailDataManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
    setupActivity()

    if (!extractDataFromIntent()) {
      finish()
      return
    }

    initializeManagers()
    setupObservers()
    loadInitialData()
  }

  private fun setupActivity() {
    binding = ActivityMediaDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.appBarLayout.handleOverHeightAppBar()
    scrollActionBarBehavior(window, binding.appBarLayout, binding.nestedScrollView)
    addPaddingWhenNavigationEnable(binding.root)
    justifyTextView(binding.tvOverview as TextView)
  }

  private fun extractDataFromIntent(): Boolean {
    val item = extractMediaItemFromIntent(intent) ?: return false
    dataExtra = item
    return true
  }

  private fun initializeManagers() {
    uiManager = DetailUIManager(
      binding = binding,
      activity = this,
      navigator = navigator,
    )

    watchProvidersManager = WatchProvidersManager(
      binding = binding,
      context = this,
      dataExtra = dataExtra,
    )

    dataManager = DetailDataManager(
      detailViewModel = detailViewModel,
      dataExtra = dataExtra,
    )

    userInteractionHandler = UserInteractionHandler(
      binding = binding,
      activity = this,
      detailViewModel = detailViewModel,
      dataExtra = dataExtra,
      uiManager = uiManager,
      dataManager = dataManager,
    )

    // observe user login state
    prefViewModel.getUserToken().observe(this) { token ->
      val isLogin = token != NAN && token.isNotEmpty()
      userInteractionHandler.setUserState(isLogin)
    }
  }

  private fun setupObservers() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        detailViewModel.uiState.collect { state ->
          renderState(state)
        }
      }
    }

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        detailViewModel.errorEvent.collect { message ->
          uiManager.showLoadingDim(false)
          uiManager.showSnackbarWarning(message)
        }
      }
    }

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        detailViewModel.toastEvent.collect { resId ->
          uiManager.showToast(getString(resId))
        }
      }
    }
  }

  private fun renderState(state: MediaDetailUiState) {
    uiManager.showLoadingDim(state.isLoading)

    state.detail?.let { detail ->
      Log.e("LLLLLLLLLLL", state.toString())
      uiManager.updateDetailUI(detail, dataExtra.mediaType)
      dataExtra = dataExtra.copy(listGenreIds = detail.genreId)
      if (!detail.imdbId.isNullOrEmpty()) {
        detailViewModel.getOMDbDetails(detail.imdbId)
      }
    }
    watchProvidersManager.handleWatchProvidersState(state.watchProviders)
    state.credits?.let { uiManager.updateCreditsUI(it) }
    state.omdbDetails?.let { uiManager.updateOMDbScores(it) }
    state.recommendations?.let { uiManager.updateRecommendations(it, lifecycle) }
    uiManager.setupTrailerButton(state.videoLink)

    userInteractionHandler.renderState(state)
  }

  private fun loadInitialData() {
    uiManager.showGeneralInfo(dataExtra)
    uiManager.showLoadingDim(true)

    dataManager.loadAllData()
  }

  override fun onDestroy() {
    super.onDestroy()
    if (::uiManager.isInitialized) {
      uiManager.cleanup()
    }
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}
