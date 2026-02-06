package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testLoadingState
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testSuccessfulCall
import com.waffiq.bazz_movies.core.test.RepositoryTestHelper.testUnsuccessfulCall
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonRepositoryImplTest
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toCombinedCredit
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PersonRepositoryImplTest : BasePersonRepositoryImplTest() {
  @Test
  fun getDetailPerson_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<DetailPersonResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getPersonDetails(id) },
      repositoryCall = { repository.getDetailPerson(id) },
      expectedData = mockResponse.toDetailPerson(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getPersonDetails(id) } }
    )
  }

  @Test
  fun getDetailPerson_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getPersonDetails(id) },
      repositoryCall = { repository.getDetailPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonDetails(id) } }
    )
  }

  @Test
  fun getDetailPerson_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getPersonDetails(id) },
      repositoryCall = { repository.getDetailPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonDetails(id) } }
    )
  }

  @Test
  fun getKnownForPerson_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<CombinedCreditResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getPersonCredits(id) },
      repositoryCall = { repository.getKnownForPerson(id) },
      expectedData = mockResponse.toCombinedCredit(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getPersonCredits(id) } }
    )
  }

  @Test
  fun getKnownForPerson_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getPersonCredits(id) },
      repositoryCall = { repository.getKnownForPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonCredits(id) } }
    )
  }

  @Test
  fun getKnownForPerson_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getPersonCredits(id) },
      repositoryCall = { repository.getKnownForPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonCredits(id) } }
    )
  }

  @Test
  fun getImagePerson_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<ImagePersonResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getPersonImages(id) },
      repositoryCall = { repository.getImagePerson(id) },
      expectedData = mockResponse.toImagePerson(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getPersonImages(id) } }
    )
  }

  @Test
  fun getImagePerson_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getPersonImages(id) },
      repositoryCall = { repository.getImagePerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonImages(id) } }
    )
  }

  @Test
  fun getImagePerson_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getPersonImages(id) },
      repositoryCall = { repository.getImagePerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonImages(id) } }
    )
  }

  @Test
  fun getExternalIDPerson_whenSuccessful_returnsSuccessResult() = runTest {
    val mockResponse = mockk<ExternalIDPersonResponse>(relaxed = true)
    testSuccessfulCall(
      mockResponse = mockResponse,
      dataSourceCall = { movieDataSource.getPersonExternalIds(id) },
      repositoryCall = { repository.getExternalIDPerson(id) },
      expectedData = mockResponse.toExternalIDPerson(),
      verifyDataSourceCall = { coVerify(atLeast = 1) { movieDataSource.getPersonExternalIds(id) } }
    )
  }

  @Test
  fun getExternalIDPerson_whenUnsuccessful_returnsErrorResult() = runTest {
    testUnsuccessfulCall(
      dataSourceCall = { movieDataSource.getPersonExternalIds(id) },
      repositoryCall = { repository.getExternalIDPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonExternalIds(id) } }
    )
  }

  @Test
  fun getExternalIDPerson_whenLoadingEmitted_returnsLoadingOutcome() = runTest {
    testLoadingState(
      dataSourceCall = { movieDataSource.getPersonExternalIds(id) },
      repositoryCall = { repository.getExternalIDPerson(id) },
      verifyDataSourceCall = { coVerify { movieDataSource.getPersonExternalIds(id) } }
    )
  }
}
