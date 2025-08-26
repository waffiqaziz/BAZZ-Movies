package com.waffiq.bazz_movies.feature.detail.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarUtils.scrollActionBarBehavior
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailDataManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailUIManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.UserInteractionHandler
import com.waffiq.bazz_movies.feature.detail.ui.manager.WatchProvidersManager
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ParcelableHelper.extractMediaItemFromIntent
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaDetailActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityDetailMovieBinding
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
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
    )
    super.onCreate(savedInstanceState)
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
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
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
      navigator = navigator
    )

    watchProvidersManager = WatchProvidersManager(
      binding = binding,
      context = this,
      dataExtra = dataExtra
    )

    dataManager = DetailDataManager(
      detailViewModel = detailViewModel,
      dataExtra = dataExtra,
      lifecycleOwner = this
    )

    userInteractionHandler = UserInteractionHandler(
      binding = binding,
      activity = this,
      detailViewModel = detailViewModel,
      dataExtra = dataExtra,
      uiManager = uiManager,
      dataManager = dataManager
    )

    // observe user login state
    prefViewModel.getUserToken().observe(this) { token ->
      val isLogin = token != NAN && token.isNotEmpty()
      userInteractionHandler.setUserState(isLogin)
    }
  }

  private fun setupObservers() {
    setupViewModelObservers()
    uiManager.setupLoadingObserver(detailViewModel.loadingState)
    uiManager.setupErrorObserver(detailViewModel.errorState)
  }

  private fun setupViewModelObservers() {
    // observe detail data changes
    detailViewModel.detailMedia.observe(this) { details ->
      uiManager.updateDetailUI(details, dataExtra.mediaType)
      dataExtra = dataExtra.copy(listGenreIds = details.genreId)

      // only for movie while tv-series is missing imdb id
      if (details.imdbId != null && details.imdbId.isNotEmpty()) {
        detailViewModel.getOMDbDetails(details.imdbId)
      }
    }

    detailViewModel.mediaCredits.observe(this) { credits ->
      uiManager.updateCreditsUI(credits)
    }

    detailViewModel.omdbResult.observe(this) { omdbScore ->
      uiManager.updateOMDbScores(omdbScore)
    }

    detailViewModel.linkVideo.observe(this) { videoLink ->
      uiManager.setupTrailerButton(videoLink)
    }

    detailViewModel.recommendation.observe(this) { recommendations ->
      uiManager.updateRecommendations(recommendations, lifecycle)
    }

    watchProvidersManager.observeWatchProviders(
      detailViewModel.watchProvidersUiState,
      this
    )
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
