package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testExternalIDPerson
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testImagesList
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testKnownForList
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just

class PersonActivityTestHelper : PersonActivityTestSetup {

  override val detailPersonLiveData = MutableLiveData<DetailPerson>()
  override val knownForLiveData = MutableLiveData<List<CastItem>>()
  override val imagePersonLiveData = MutableLiveData<List<ProfilesItem>>()
  override val externalIdPersonLiveData = MutableLiveData<ExternalIDPerson>()
  override val errorStateLiveData = MutableLiveData<Event<String>>()
  override val loadingStateLiveData = MutableLiveData<Boolean>()
  override lateinit var context: Context

  override fun setupBaseMocks() {
    loadingStateLiveData.postValue(false)
    detailPersonLiveData.postValue(testDetailPerson)
    knownForLiveData.postValue(testKnownForList)
    imagePersonLiveData.postValue(testImagesList)
    externalIdPersonLiveData.postValue(testExternalIDPerson)
  }

  override fun setupViewModelMocks(mockPersonViewModel: PersonViewModel) {
    every { mockPersonViewModel.detailPerson } returns detailPersonLiveData
    every { mockPersonViewModel.knownFor } returns knownForLiveData
    every { mockPersonViewModel.imagePerson } returns imagePersonLiveData
    every { mockPersonViewModel.externalIdPerson } returns externalIdPersonLiveData
    every { mockPersonViewModel.errorState } returns errorStateLiveData
    every { mockPersonViewModel.loadingState } returns loadingStateLiveData

    every { mockPersonViewModel.getDetailPerson(any()) } just Runs
    every { mockPersonViewModel.getKnownFor(any()) } just Runs
    every { mockPersonViewModel.getImagePerson(any()) } just Runs
    every { mockPersonViewModel.getExternalIDPerson(any()) } just Runs
  }

  override fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs
  }

  override fun shortDelay() {
    onView(isRoot()).perform(waitFor(300))
  }

  override fun initializeTest(context: Context) {
    this.context = context
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      Glide.get(context).clearMemory()
    }
  }
}
