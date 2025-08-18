package com.waffiq.bazz_movies.feature.detail.ui.manager

import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.string.cant_provide_a_score
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available_full
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_added_successfully
import com.waffiq.bazz_movies.core.designsystem.R.style.CustomAlertDialogTheme
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.feature.detail.R.id.btn_cancel
import com.waffiq.bazz_movies.feature.detail.R.id.btn_submit
import com.waffiq.bazz_movies.feature.detail.R.id.rating_bar_action
import com.waffiq.bazz_movies.feature.detail.R.layout.dialog_rating
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.ui.state.UserAuthState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnFavoriteBG
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnWatchlistBG
import kotlin.math.roundToInt

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
  private val binding: ActivityDetailMovieBinding,
  private val activity: AppCompatActivity,
  private val detailViewModel: MediaDetailViewModel,
  private val dataExtra: MediaItem,
  private val uiManager: DetailUIManager,
  private val dataManager: DetailDataManager,
) {
  private var favorite = false
  private var watchlist = false
  private var userState: UserAuthState = UserAuthState.NotInitialized

  /** Initializes the UserInteractionHandler with necessary setup.
   * This includes setting up tags for buttons, user state, click listeners,
   * and observing favorite/watchlist post results.
   *
   * @param activity The activity context where this handler is used.
   */
  init {
    initializeTags()
    setupUser(activity)
    setupClickListeners()
    observeFavoriteWatchlistPost(activity)
  }

  /**
   * Sets the user state to either logged in or guest and updates the UI accordingly.
   *
   * @param isLogin Boolean indicating if the user is logged in.
   */
  fun setUserState(isLogin: Boolean) {
    userState = if (isLogin) UserAuthState.LoggedIn else UserAuthState.Guest
    setupUser(activity)
  }

  // only for test purposes, to cover test cases where user state is not initialized
  @VisibleForTesting(otherwise = VisibleForTesting.NONE)
  fun resetUserStateForTest() {
    userState = UserAuthState.NotInitialized
  }

  /** Initializes button tags for favorite and watchlist actions. */
  private fun initializeTags() {
    binding.btnFavorite.tag = ic_hearth
    binding.btnWatchlist.tag = ic_bookmark
  }

  /**
   * Observes the user token to determine login state and setup appropriate observers.
   */
  private fun setupUser(lifecycleOwner: LifecycleOwner) {

    when (userState) {
      is UserAuthState.LoggedIn -> {
        binding.yourScoreViewGroup.isVisible = true
        setupLoginUserObservers(lifecycleOwner)
        observeRatingState(activity)
        binding.yourScoreViewGroup.setOnClickListener { showDialogRate() }
      }

      is UserAuthState.Guest -> {
        binding.yourScoreViewGroup.isVisible = false
        setupGuestUserObservers(lifecycleOwner)
      }

      is UserAuthState.NotInitialized -> {
        binding.yourScoreViewGroup.isVisible = false
        // do nothing, wait for user state to be set
      }
    }
  }

  /**
   * Observes data for a logged-in user such as favorite/watchlist state and ratings.
   */
  private fun setupLoginUserObservers(lifecycleOwner: LifecycleOwner) {
    getMediaState()

    detailViewModel.itemState.observe(lifecycleOwner) { state ->
      state?.let {
        favorite = it.favorite
        watchlist = it.watchlist
        showRatingUserLogin(it)
        changeBtnFavoriteBG(binding.btnFavorite, it.favorite)
        changeBtnWatchlistBG(binding.btnWatchlist, it.watchlist)
      }
    }
  }

  /**
   * Observes data from local DB for guest users (not logged in).
   */
  private fun setupGuestUserObservers(lifecycleOwner: LifecycleOwner) {
    // guest user observers
    detailViewModel.isFavoriteDB(dataExtra.id, dataExtra.mediaType)
    detailViewModel.isFavorite.observe(lifecycleOwner) { isFav ->
      changeBtnFavoriteBG(binding.btnFavorite, isFav)
      favorite = isFav
    }

    detailViewModel.isWatchlistDB(dataExtra.id, dataExtra.mediaType)
    detailViewModel.isWatchlist.observe(lifecycleOwner) { isWatch ->
      changeBtnWatchlistBG(binding.btnWatchlist, isWatch)
      watchlist = isWatch
    }
  }

  /**
   * Observes result of add/remove item from favorite/watchlist.
   */
  private fun observeFavoriteWatchlistPost(lifecycleOwner: LifecycleOwner) {
    detailViewModel.postModelState.observe(lifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled()?.let { postModelState ->
        if (!postModelState.isSuccess) return@let

        val messageResId = when {
          postModelState.isDelete && postModelState.isFavorite -> item_removed_from_favorite
          postModelState.isDelete -> item_removed_from_watchlist
          postModelState.isFavorite -> item_added_to_favorite
          else -> item_added_to_watchlist
        }

        uiManager.showToast(activity.getString(messageResId))
      }
    }
  }

  /**
   * Observes the result of submit a rating.
   */
  private fun observeRatingState(lifecycleOwner: LifecycleOwner) {
    detailViewModel.rateState.observe(lifecycleOwner) { eventResult ->
      eventResult.getContentIfNotHandled()?.let { isRateSuccessful ->
        if (isRateSuccessful) {
          uiManager.showToast(activity.getString(rating_added_successfully))
        }
      }
    }
  }

  /** Sets up all UI click listeners related to user interactions. */
  private fun setupClickListeners() {
    binding.apply {
      btnBack.setOnClickListener {
        activity.finish()
      }

      btnFavorite.setOnClickListener {
        handleFavoriteClick()
      }

      btnWatchlist.setOnClickListener {
        handleWatchlistClick()
      }

      swipeRefresh.setOnRefreshListener {
        handleSwipeRefresh()
      }

      // score click listeners
      imdbViewGroup.setOnClickListener {
        showDialogIfNotRated(tvScoreImdb.text.toString())
      }
      tmdbViewGroup.setOnClickListener {
        showDialogIfNotRated(tvScoreTmdb.text.toString())
      }
      metascoreViewGroup.setOnClickListener {
        showDialogIfNotRated(tvScoreMetascore.text.toString())
      }
      rottenTomatoesViewGroup.setOnClickListener {
        showDialogIfNotRated(tvScoreRottenTomatoes.text.toString())
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
      val fav = FavoriteModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      detailViewModel.postFavorite(fav)
    } else {
      watchlist = !state
      val wtc = WatchlistModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      detailViewModel.postWatchlist(wtc)
    }
  }

  /**
   * Displays the user's current rating in the UI after submit a rating.
   */
  private fun showRatingUserLogin(state: MediaState) {
    binding.tvScoreYourScore.text = when (val rating = state.rated) {
      is Rated.Unrated -> activity.getString(not_available)
      is Rated.Value -> rating.value.toString()
    }
  }

  /** Shows a "not rated" dialog if the score string has no valid number. */
  private val showDialogIfNotRated: (String) -> Unit = { scoreText ->
    if (!scoreText.contains("[0-9]".toRegex())) {
      showDialogNotRated()
    }
  }

  /** Displays a dialog to indicate that rating data is unavailable. */
  private fun showDialogNotRated() {
    MaterialAlertDialogBuilder(activity, CustomAlertDialogTheme).apply {
      setTitle(activity.resources.getString(not_available_full))
      setMessage(activity.resources.getString(cant_provide_a_score))
    }.show()
  }

  /** Shows the rating dialog for user to submit a rating. */
  private fun showDialogRate() {
    val dialog = Dialog(activity)
    val dialogView = View.inflate(activity, dialog_rating, null)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(dialogView)
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

    val ratingBar = dialogView.findViewById<RatingBar>(rating_bar_action)
    val rateNow = binding.tvScoreYourScore.text.toString()
    ratingBar.rating =
      if (rateNow == activity.getString(not_available)) 0.0f else rateNow.toFloat() / 2

    val btnSubmit: Button = dialogView.findViewById(btn_submit)
    btnSubmit.setOnClickListener {
      val rating: Float = ratingBar.rating * 2
      if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) {
        detailViewModel.postMovieRate(rating, dataExtra.id)
      } else {
        detailViewModel.postTvRate(rating, dataExtra.id)
      }

      // update rating display
      detailViewModel.rateState.observe(activity) { eventResult ->
        eventResult.peekContent().let { isRateSuccessful ->
          if (isRateSuccessful) {
            binding.tvScoreYourScore.text =
              ((rating * ROUNDING_FACTOR).roundToInt() / ROUNDING_FACTOR.toDouble()).toString()
          }
        }
      }
      dialog.dismiss()
    }

    val btnCancel: Button = dialogView.findViewById(btn_cancel)
    btnCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }

  companion object {
    private const val ROUNDING_FACTOR = 10
  }
}
