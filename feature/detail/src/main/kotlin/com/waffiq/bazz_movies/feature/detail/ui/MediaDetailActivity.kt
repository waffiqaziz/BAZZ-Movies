package com.waffiq.bazz_movies.feature.detail.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarUtils.scrollActionBarBehavior
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailMovieDataManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.DetailMovieUIManager
import com.waffiq.bazz_movies.feature.detail.ui.manager.UserInteractionHandler
import com.waffiq.bazz_movies.feature.detail.ui.manager.WatchProvidersManager
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
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

  private lateinit var uiManager: DetailMovieUIManager
  private lateinit var watchProvidersManager: WatchProvidersManager
  private lateinit var userInteractionHandler: UserInteractionHandler
  private lateinit var dataManager: DetailMovieDataManager

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

  @Suppress("ReturnCount")
  private fun extractDataFromIntent(): Boolean {
    if (!intent.hasExtra(EXTRA_MOVIE)) return false

    dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_MOVIE, MediaItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_MOVIE)
    } ?: return false

    return true
  }

  private fun initializeManagers() {
    uiManager = DetailMovieUIManager(
      binding = binding,
      activity = this,
      navigator = navigator
    )

    watchProvidersManager = WatchProvidersManager(
      binding = binding,
      context = this,
      dataExtra = dataExtra
    )

    dataManager = DetailMovieDataManager(
      detailViewModel = detailViewModel,
      prefViewModel = prefViewModel,
      dataExtra = dataExtra,
      lifecycleOwner = this
    )

    userInteractionHandler = UserInteractionHandler(
      binding = binding,
      activity = this,
      detailViewModel = detailViewModel,
      prefViewModel = prefViewModel,
      dataExtra = dataExtra,
      uiManager = uiManager,
      dataManager = dataManager
    )
  }

  private fun setupObservers() {
    setupViewModelObservers()
    setupUserStateObservers()
    uiManager.setupLoadingObserver(detailViewModel.loadingState)
    uiManager.setupErrorObserver(detailViewModel.errorState)
  }

  private fun setupViewModelObservers() {
    // observe detail data changes
    detailViewModel.detailMedia.observe(this) { details ->
      uiManager.updateDetailUI(details, dataExtra.mediaType)
      dataExtra = dataExtra.copy(listGenreIds = details.genreId)

      // only for movie while tv-series is missing imdb id
      details.imdbId?.takeIf { it.isNotEmpty() }?.let { imdbId ->
        detailViewModel.getOMDbDetails(imdbId)
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

  private fun setupUserStateObservers() {
    userInteractionHandler.setupUserStateObservers()
    userInteractionHandler.setupClickListeners()
  }

  private fun loadInitialData() {
    uiManager.showGeneralInfo(dataExtra)
    uiManager.showLoadingDim(true)

    dataManager.loadAllData()
  }

  override fun onDestroy() {
    super.onDestroy()
    uiManager.cleanup()
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}
