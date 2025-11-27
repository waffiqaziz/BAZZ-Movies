package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.ViewAction
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackBarUserLoginData
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import com.waffiq.bazz_movies.feature.favorite.ui.viewmodel.FavoriteViewModel

interface FavoriteFragmentTestHelper {

  var favoriteFragment: FavoriteFragment

  var mockUserModel: MutableLiveData<UserModel>
  var mockFavoriteMoviesFromDB: MutableLiveData<List<Favorite>>
  var mockFavoriteTvFromDB: MutableLiveData<List<Favorite>>
  var mockUndoDB: MutableLiveData<Event<Favorite>>
  var mockDbResult: MutableLiveData<Event<DbResult<Int>>>
  var mockSnackBarAlready: MutableLiveData<Event<String>>
  var mockSnackBarAdded: MutableLiveData<Event<SnackBarUserLoginData>>

  fun setupMocks(userPreferenceViewModel: UserPreferenceViewModel)

  fun loggedUser(favoriteViewModel: FavoriteViewModel)

  fun guestUser(sharedDBViewModel: SharedDBViewModel)

  fun launchFragment()

  fun performSwipeActions()
  fun performSwipeAction(position: Int, viewAction: ViewAction)
  fun performPullToRefresh()
  fun performUndoAction()
}
