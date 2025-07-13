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

interface PersonActivityTestSetup {
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
