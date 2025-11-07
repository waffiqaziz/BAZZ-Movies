package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.favoriteMovie2
import com.waffiq.bazz_movies.feature.favorite.testutils.DataDump.userModel
import com.waffiq.bazz_movies.feature.favorite.ui.fragment.FavoriteFragment
import io.mockk.every

class DefaultFavoriteFragmentTestHelper : FavoriteFragmentTestHelper {

  override lateinit var favoriteFragment: FavoriteFragment

  override val mockUserModel = MutableLiveData<UserModel>()

  // Mocks for SharedDBViewModel
  override val mockFavoriteMoviesFromDB = MutableLiveData<List<Favorite>>()
  override val mockUndoDB = MutableLiveData<Event<Favorite>>()
  override val mockDbResult = MutableLiveData<Event<DbResult<Int>>>()

  override fun setupMocks(userPreferenceViewModel: UserPreferenceViewModel) {
    every { userPreferenceViewModel.getUserPref() } returns mockUserModel
  }

  override fun loggedUser() {
    mockUserModel.postValue(userModel)
  }

  override fun guestUser(sharedDBViewModel: SharedDBViewModel) {
    every { sharedDBViewModel.favoriteMoviesFromDB } returns mockFavoriteMoviesFromDB
    every { sharedDBViewModel.undoDB } returns mockUndoDB
    every { sharedDBViewModel.dbResult } returns mockDbResult

    mockUserModel.postValue(userModel.copy(token = NAN))
    Thread.sleep(500)
    mockFavoriteMoviesFromDB.postValue(listOf(favoriteMovie, favoriteMovie2))
  }
}
