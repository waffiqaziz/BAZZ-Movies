package com.waffiq.bazz_movies.feature.favorite.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.waffiq.bazz_movies.core.common.utils.Constants
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.BaseViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.GeneralHelper
import com.waffiq.bazz_movies.feature.favorite.databinding.FragmentFavoriteChildBinding
import com.waffiq.bazz_movies.feature.favorite.ui.delegate.GuestUserDelegate
import com.waffiq.bazz_movies.feature.favorite.ui.delegate.LoggedUserDelegate
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import javax.inject.Inject

/**
 * A base fragment class for displaying and managing favorite items.
 * This class handles both logged-in and guest user states, providing functionality
 * for adding/removing favorites and watchlist items, as well as swipe actions
 * with undo capabilities via Snackbar.
 *
 * @param T The type of data, which [MediaItem].
 */
abstract class BaseFavoriteFragment<T : Any> : Fragment() {

  @Inject
  lateinit var iSnackbar: ISnackbar

  @Inject
  lateinit var navigator: INavigator

  // ViewModels
  protected val favoriteViewModel: FavoriteViewModel by viewModels()
  protected val sharedDBViewModel: SharedDBViewModel by viewModels()
  protected val userPreferenceViewModel: UserPreferenceViewModel by viewModels()
  protected val baseViewModel: BaseViewModel by viewModels({ requireParentFragment() })

  // Delegates
  private var loggedUserDelegate: LoggedUserDelegate? = null
  private var guestUserDelegate: GuestUserDelegate? = null

  protected abstract val binding: FragmentFavoriteChildBinding
  protected abstract fun getMediaType(): String

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    observeUserState()
  }

  private fun setupRecyclerView() {
    binding.rvFavorite.apply {
      layoutManager = GeneralHelper.initLinearLayoutManagerVertical(requireContext())
      itemAnimator = DefaultItemAnimator()
    }
  }

  private fun observeUserState() {
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != Constants.NAN) {
        setupLoggedUser(user.token)
      } else {
        setupGuestUser()
      }
    }
  }

  private fun setupLoggedUser(token: String) {
    guestUserDelegate?.cleanup()
    guestUserDelegate = null

    loggedUserDelegate = LoggedUserDelegate(
      fragment = this,
      binding = binding,
      favoriteViewModel = favoriteViewModel,
      baseViewModel = baseViewModel,
      mediaType = getMediaType(),
      iSnackbar = iSnackbar,
      navigator = navigator,
    ).apply {
      setup(token)
    }
  }

  private fun setupGuestUser() {
    loggedUserDelegate?.cleanup()
    loggedUserDelegate = null

    guestUserDelegate = GuestUserDelegate(
      fragment = this,
      binding = binding,
      navigator = navigator,
      sharedDBViewModel = sharedDBViewModel,
      mediaType = getMediaType(),
    ).apply {
      setup()
    }
  }

  override fun onResume() {
    super.onResume()
    baseViewModel.resetSnackbarShown()
    userPreferenceViewModel.getUserPref().observe(viewLifecycleOwner) { user ->
      if (user.token != Constants.NAN) {
        loggedUserDelegate?.refresh()
      }
    }
  }

  override fun onPause() {
    super.onPause()
    loggedUserDelegate?.cleanup()
    guestUserDelegate?.cleanup()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    loggedUserDelegate?.cleanup()
    guestUserDelegate?.cleanup()
    loggedUserDelegate = null
    guestUserDelegate = null
  }
}
