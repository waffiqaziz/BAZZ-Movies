package com.waffiq.bazz_movies.core.user.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.core.user.testutils.HelperVariableTest.userModelPref
import com.waffiq.bazz_movies.core.user.utils.common.Constants.DATASTORE_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserPreferenceNonMockTest {

  private lateinit var testContext: Context
  private lateinit var testDataStore: DataStore<Preferences>
  private lateinit var userPreference: UserPreference

  @get:Rule
  val testCoroutineDispatcher = UnconfinedDispatcherRule()

  @Before
  fun setup() = runTest {
    testContext = ApplicationProvider.getApplicationContext()
    testDataStore = PreferenceDataStoreFactory.create(
      produceFile = { testContext.preferencesDataStoreFile(DATASTORE_NAME) }
    )
    userPreference = UserPreference(testDataStore)
  }

  @Test
  fun saveUserAndGetUser_whenCalled_storesAndReturnsSameData() = runTest {
    userPreference.saveUser(userModelPref)

    val savedUser = userPreference.getUser().first()
    assertEquals(userModelPref, savedUser)
  }

  @Test
  fun saveRegion_whenSuccessful_storesCorrectRegion() = runTest {
    userPreference.saveUser(userModelPref)
    userPreference.saveRegion("MY")

    val savedUser = userPreference.getUser().first()
    assertEquals("MY", savedUser.region)
  }

  @Test
  fun saveRegion_withEmptyRegion_storesEmptyRegion() = runTest {
    userPreference.saveUser(userModelPref)
    userPreference.saveRegion("")
    assertEquals("", userPreference.getUser().first().region)
  }

  @Test
  fun getToken_whenSuccessful_returnsCorrectToken() = runTest {
    userPreference.saveUser(userModelPref)

    val userToken = userPreference.getToken().first()
    assertEquals("sampleToken", userToken)
  }

  @Test
  fun getRegion_whenSuccessful_returnsCorrectRegion() = runTest {
    userPreference.saveUser(userModelPref)

    val userToken = userPreference.getRegion().first()
    assertEquals("US", userToken)
  }

  @Test
  fun removeUserData_whenSuccessful_removesAllData() = runTest {
    userPreference.saveUser(userModelPref)

    // check first if data is valid
    assertEquals(userModelPref, userPreference.getUser().first())

    // call remove function
    userPreference.removeUserData()

    val updatedDataUser = userPreference.getUser().first()
    assertEquals(0, updatedDataUser.userId)
    assertEquals("", updatedDataUser.name)
  }
}
