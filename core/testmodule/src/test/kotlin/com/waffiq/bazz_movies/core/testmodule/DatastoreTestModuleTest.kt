package com.waffiq.bazz_movies.core.testmodule

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.network.data.remote.datasource.auth.AuthRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.country.CountryRemoteDataSource
import com.waffiq.bazz_movies.core.user.data.repository.UserRepositoryImpl
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatastoreTestModuleTest {

  private lateinit var context: Context

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
  }

  @Test
  fun provideTestDataStore_success_shouldReturnDataStore() {
    val dataStore = DatastoreTestModule.provideTestDataStore(context)

    assertNotNull(dataStore)
  }

  @Test
  fun provideUserPreference_success_shouldReturnUserPreference() {
    val dataStore = DatastoreTestModule.provideTestDataStore(context)

    val preference = DatastoreTestModule.provideUserPreference(dataStore)

    assertNotNull(preference)
  }

  @Test
  fun provideUserRepository_success_shouldReturnRepositoryImpl() {
    val dataStore = DatastoreTestModule.provideTestDataStore(context)
    val preference = DatastoreTestModule.provideUserPreference(dataStore)

    val authRemote = mockk<AuthRemoteDataSource>(relaxed = true)
    val countryRemote = mockk<CountryRemoteDataSource>(relaxed = true)

    val repository = DatastoreTestModule.provideUserRepository(
      preference,
      authRemote,
      countryRemote,
    )

    assertNotNull(repository)
    assertTrue(repository is UserRepositoryImpl)
  }

  @Test
  fun provideTestDataStore_shouldInitialize() =
    runTest {
      val context = ApplicationProvider.getApplicationContext<Context>()
      val dataStore = DatastoreTestModule.provideTestDataStore(context)

      dataStore.data.first()

      assertNotNull(dataStore)
    }
}
