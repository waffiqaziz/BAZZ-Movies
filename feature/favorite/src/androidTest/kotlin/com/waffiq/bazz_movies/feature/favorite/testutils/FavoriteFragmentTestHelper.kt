package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import org.junit.Before

interface FavoriteFragmentTestHelper {

  var favoriteFragment: FavoriteFragment

  val mockUserModel: MutableLiveData<UserModel>
  val mockFavoriteMoviesFromDB : MutableLiveData<List<Favorite>>
  val mockUndoDB : MutableLiveData<Event<Favorite>>
  val mockDbResult : MutableLiveData<Event<DbResult<Int>>>

  fun setupMocks(userPreferenceViewModel: UserPreferenceViewModel)

  fun loggedUser()

  fun guestUser(sharedDBViewModel: SharedDBViewModel)

  @Before
  fun setUp() {
    favoriteFragment = launchFragmentInHiltContainer<FavoriteFragment>()
  }
}
