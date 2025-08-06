package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import com.waffiq.bazz_movies.navigation.INavigator

/**
 * Interface for providing test utilities for PersonActivity.
 *
 * This interface defines methods to set up mock data and dependencies for testing the Person Activity.
 * It includes LiveData objects for person details, known for movies, images, external IDs, error states,
 * and loading states.
 */
interface PersonActivityTestHelper {
  val detailPersonLiveData: MutableLiveData<DetailPerson>
  val knownForLiveData: MutableLiveData<List<CastItem>>
  val imagePersonLiveData: MutableLiveData<List<ProfilesItem>>
  val externalIdPersonLiveData: MutableLiveData<ExternalIDPerson>
  val errorStateLiveData: MutableLiveData<Event<String>>
  val loadingStateLiveData: MutableLiveData<Boolean>
  var context: Context

  fun setupBaseMocks()
  fun setupViewModelMocks(mockPersonViewModel: PersonViewModel)
  fun setupNavigatorMocks(mockNavigator: INavigator)
  fun initializeTest(context: Context)
}
