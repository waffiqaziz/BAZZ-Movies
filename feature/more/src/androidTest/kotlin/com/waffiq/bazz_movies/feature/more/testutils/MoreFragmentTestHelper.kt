package com.waffiq.bazz_movies.feature.more.testutils

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.ui.MoreFragment
import com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before

interface MoreFragmentTestHelper {
  var moreFragment: MoreFragment

  val mockRegionPref: MutableLiveData<String>
  val mockUIState: MutableStateFlow<UIState<Unit>>
  val mockCountryCode: MutableLiveData<String>
  val mockUserModel: MutableLiveData<UserModel>

  fun setupMocks(
    mockNavigator: INavigator,
    mockSnackbar: ISnackbar,
  )

  fun setupViewModelMocks(
    mockMoreLocalViewModel: MoreLocalViewModel,
    mockUserViewModel: MoreUserViewModel,
    mockRegionViewModel: RegionViewModel,
    mockUserPrefViewModel: UserPreferenceViewModel,
  )

  @Before
  fun setUp() {
    Intents.init()
    moreFragment = launchFragmentInHiltContainer<MoreFragment>()
  }

  @After
  fun tearDown() {
    Intents.release()
  }
}
