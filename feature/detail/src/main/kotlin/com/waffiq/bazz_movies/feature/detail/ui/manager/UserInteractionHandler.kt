package com.waffiq.bazz_movies.feature.detail.ui.manager

import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth_selected
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_watchlist_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_watchlist_outlined
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.utils.DetailDataUtils.titleHandler
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityMediaDetailBinding
import com.waffiq.bazz_movies.feature.detail.ui.dialog.RateDialog
import com.waffiq.bazz_movies.feature.detail.ui.fragment.CreditsBottomSheet
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.state.UserAuthState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.backdropPathSource
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnAction

/**
 * Handles all user interactions within the movie detail screen.
 *
 * This includes managing login state, updating UI based on user preferences
 * (e.g., favorites, watchlist), responding to rating interactions, and
 * posting data to TMDB or the local database.
 *
 * @param binding View binding for the detail movie activity layout.
 * @param activity Reference to the hosting [AppCompatActivity].
 * @param detailViewModel ViewModel responsible for detail-related data operations.
 * @param dataExtra The movie or TV show data passed via intent.
 * @param uiManager Utility class for managing UI feedback like toast/snackbar.
 * @param dataManager Utility class for managing data refresh or reload logic.
 */
class UserInteractionHandler(
  private val binding: ActivityMediaDetailBinding,
  private val activity: AppCompatActivity,
  private val detailViewModel: MediaDetailViewModel,
  private val dataExtra: MediaItem,
  private val uiManager: DetailUIManager,
  private val dataManager: DetailDataManager,
) {
  private var favorite = false
  private var watchlist = false
  private var userState: UserAuthState = UserAuthState.NotInitialized

  /* Initializes the UserInteractionHandler with necessary setup.
   * This includes setting up tags for buttons, user state, click listeners,
   * and observing favorite/watchlist post results.
   *
   * @param activity The activity context where this handler is used.
   */
  init {
    initializeTags()
    setupUser()
    setupClickListeners()
  }

  /**
   * Sets the user state to either logged in or guest and updates the UI accordingly.
   *
   * @param isLogin Boolean indicating if the user is logged in.
   */
  fun setUserState(isLogin: Boolean) {
    userState = if (isLogin) UserAuthState.LoggedIn else UserAuthState.Guest
    setupUser()
  }

  // only for test purposes, to cover test cases where user state is not initialized
  @VisibleForTesting(otherwise = VisibleForTesting.NONE)
  fun resetUserStateForTest() {
    userState = UserAuthState.NotInitialized
  }

  /** Initializes button tags for favorite and watchlist actions. */
  private fun initializeTags() {
    binding.btnFavorite.tag = ic_hearth
    binding.btnWatchlist.tag = ic_watchlist_outlined
  }

  /**
   * Observes the user token to determine login state and setup appropriate observers.
   */
  private fun setupUser() {
    when (userState) {
      is UserAuthState.LoggedIn -> {
        binding.yourScoreViewGroup.isVisible = true
        binding.yourScoreViewGroup.setOnClickListener { showDialogRate() }
        getMediaState()
      }

      is UserAuthState.Guest -> {
        binding.yourScoreViewGroup.isVisible = false
        detailViewModel.isFavoriteDB(dataExtra.id, dataExtra.mediaType)
        detailViewModel.isWatchlistDB(dataExtra.id, dataExtra.mediaType)
      }

      is UserAuthState.NotInitialized -> {
        binding.yourScoreViewGroup.isVisible = false
        // do nothing, wait for user state to be set
      }
    }
  }

  fun renderState(state: MediaDetailUiState) {
    favorite = state.isFavorite
    watchlist = state.isWatchlist

    changeBtnAction(
      button = binding.btnFavorite,
      isActivated = state.isFavorite,
      iconActive = ic_hearth_selected,
      iconInactive = ic_hearth,
    )
    changeBtnAction(
      button = binding.btnWatchlist,
      isActivated = state.isWatchlist,
      iconActive = ic_watchlist_filled,
      iconInactive = ic_watchlist_outlined,
    )

    state.itemState?.let { showRatingUserLogin(it) }

    state.mediaStateResult?.let { result ->
      if (result.isSuccess) {
        val msgRes = when {
          result.isDelete && result.isFavorite -> item_removed_from_favorite
          result.isDelete -> item_removed_from_watchlist
          result.isFavorite -> item_added_to_favorite
          else -> item_added_to_watchlist
        }
        uiManager.showToast(activity.getString(msgRes))
        detailViewModel.consumeMediaStateResult()
      }
    }
  }

  /** Sets up all UI click listeners related to user interactions. */
  private fun setupClickListeners() {
    binding.apply {
      btnBack.setOnClickListener { activity.finish() }
      btnFavorite.setOnClickListener { handleFavoriteClick() }
      btnWatchlist.setOnClickListener { handleWatchlistClick() }
      btnSidebar.setOnClickListener { uiManager.showSideSheet() }
      swipeRefresh.setOnRefreshListener { handleSwipeRefresh() }
      binding.btnViewAllCast.setOnClickListener {
        CreditsBottomSheet().show(
          activity.supportFragmentManager,
          CreditsBottomSheet::class.java.simpleName,
        )
      }
      btnMoreRecommendation.button.setOnClickListener {
        uiManager.openListPage(
          mediaType = dataExtra.mediaType,
          title = titleHandler(dataExtra),
          backdrop = dataExtra.backdropPathSource,
          mediaId = dataExtra.id,
        )
      }
    }
  }

  /** Handles favorite button click based on login state. */
  private fun handleFavoriteClick() {
    uiManager.dismissSnackbar()

    when (userState) {
      is UserAuthState.Guest -> {
        detailViewModel.handleBtnFavorite(favorite, watchlist, dataExtra)
      }

      is UserAuthState.LoggedIn -> {
        postDataToTMDB(isModeFavorite = true, state = favorite)
      }

      else -> {
        // Not initialized, do nothing
      }
    }
  }

  /** Handles watchlist button click based on login state. */
  private fun handleWatchlistClick() {
    uiManager.dismissSnackbar()

    when (userState) {
      is UserAuthState.Guest -> {
        detailViewModel.handleBtnWatchlist(favorite, watchlist, dataExtra)
      }

      is UserAuthState.LoggedIn -> {
        postDataToTMDB(isModeFavorite = false, state = watchlist)
      }

      else -> {
        // Not initialized, do nothing
      }
    }
  }

  /** Refreshes data and user states when swipe refresh is triggered. */
  private fun handleSwipeRefresh() {
    // refresh user login media state
    if (userState is UserAuthState.LoggedIn) {
      getMediaState()
    }

    // refresh detail data based on media type
    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> {
        dataManager.loadAllData()
      }

      TV_MEDIA_TYPE -> {
        dataManager.loadAllData()
      }
    }
    binding.swipeRefresh.isRefreshing = false
  }

  /**
   * Retrieves stated data (favorite/watchlist) from TMDB based on media type.
   */
  private fun getMediaState() {
    if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) {
      detailViewModel.getMovieState(dataExtra.id)
    } else {
      detailViewModel.getTvState(dataExtra.id)
    }
  }

  /**
   * Sends a request to TMDB to update favorite or watchlist status.
   *
   * @param isModeFavorite Determines if action is for favorite or watchlist.
   * @param state Current state of the button. The state is used to determinate the operation,
   * if its **true** perform delete, if its **false** perform add
   */
  private fun postDataToTMDB(isModeFavorite: Boolean, state: Boolean) {
    if (isModeFavorite) {
      favorite = !state
      val fav = FavoriteParams(
        dataExtra.mediaType,
        dataExtra.id,
        !state,
      )
      detailViewModel.postFavorite(fav)
    } else {
      watchlist = !state
      val wtc = WatchlistParams(
        dataExtra.mediaType,
        dataExtra.id,
        !state,
      )
      detailViewModel.postWatchlist(wtc)
    }
  }

  /**
   * Displays the user's current rating in the UI after submit a rating.
   */
  fun showRatingUserLogin(state: MediaState) {
    binding.tvScoreYourScore.text = when (val rating = state.rated) {
      is Rated.Unrated -> activity.getString(not_available)
      is Rated.Value -> rating.value.toString()
    }
  }

  /** Shows the rating dialog for user to submit a rating. */
  private fun showDialogRate() {
    val rateNow = binding.tvScoreYourScore.text.toString()

    RateDialog(
      context = activity,
      currentRating = rateNow,
    ) { rating ->
      // Submit to ViewModel
      if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) {
        detailViewModel.postMovieRate(rating, dataExtra.id)
      } else {
        detailViewModel.postTvRate(rating, dataExtra.id)
      }
    }.show()
  }
}
