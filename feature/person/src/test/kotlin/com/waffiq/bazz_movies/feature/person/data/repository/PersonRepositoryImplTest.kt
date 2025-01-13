package com.waffiq.bazz_movies.feature.person.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toCombinedCredit
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonRepositoryImplTest {

  // Arrange common
  private val id = 1
  private val errorMessage = "Error occurred"

  private lateinit var repository: PersonRepositoryImpl
  private val movieDataSource: MovieDataSource = mockk() // Mock MovieDataSource

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @Before
  fun setUp() {
    repository = PersonRepositoryImpl(movieDataSource)
  }

  @Test
  fun getDetailPerson_returnsSuccessResult() = runTest {
    val mockResponse = mockk<DetailPersonResponse>(relaxed = true)
    val networkResult = NetworkResult.Success(mockResponse)

    // Mock the MovieDataSource's getDetailPerson method
    coEvery { movieDataSource.getDetailPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock success result
    }

    // Act
    val result = repository.getDetailPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Success) // Check if the second emission is Success
    val successResult = result[1] as Outcome.Success
    assertEquals(
      mockResponse.toDetailPerson(),
      successResult.data
    ) // Verify that the data is correct

    // Verify that getDetailPerson was called at least once with the expected id
    coVerify(atLeast = 1) { movieDataSource.getDetailPerson(id) }
  }

  @Test
  fun getDetailPerson_returnsErrorResult() = runTest {
    val networkResult = NetworkResult.Error(errorMessage)

    // Mock the MovieDataSource's getDetailPerson method
    coEvery { movieDataSource.getDetailPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock error result
    }

    // Act
    val result = repository.getDetailPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Error) // Check if the second emission is Error
    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message) // Verify the error message is correct

    // Verify that getDetailPerson was called exactly once with the expected id
    coVerify { movieDataSource.getDetailPerson(id) }
  }

  @Test
  fun getDetailPerson_returnsLoadingState() = runTest {
    // Mock the MovieDataSource's getDetailPerson method to emit only loading
    coEvery { movieDataSource.getDetailPerson(id) } returns flow {
      emit(NetworkResult.Loading) // Emit only Loading
    }

    // Act
    val result = repository.getDetailPerson(id).first()

    // Assert
    assert(result is Outcome.Loading) // Ensure the first emission is Loading

    // Verify that getDetailPerson was called exactly once with the expected id
    coVerify { movieDataSource.getDetailPerson(id) }
  }

  @Test
  fun getKnownForPerson_returnsSuccessResult() = runTest {
    // Arrange
    val combinedCreditResponse = mockk<CombinedCreditResponse>(relaxed = true) // Mock response
    val networkResult = NetworkResult.Success(combinedCreditResponse)

    // Mock the MovieDataSource's getKnownForPerson method
    coEvery { movieDataSource.getKnownForPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock success result
    }

    // Act
    val result = repository.getKnownForPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Success) // Check if the second emission is Success
    val successResult = result[1] as Outcome.Success
    assertEquals(
      combinedCreditResponse.toCombinedCredit(),
      successResult.data
    ) // Verify that the data is correct

    // Verify that getKnownForPerson was called exactly once with the expected id
    coVerify { movieDataSource.getKnownForPerson(id) }
  }

  @Test
  fun getKnownForPerson_returnsErrorResult() = runTest {
    // Arrange
    val networkResult = NetworkResult.Error(errorMessage)

    // Mock the MovieDataSource's getKnownForPerson method
    coEvery { movieDataSource.getKnownForPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock error result
    }

    // Act
    val result = repository.getKnownForPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Error) // Check if the second emission is Error
    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message) // Verify the error message is correct

    // Verify that getKnownForPerson was called exactly once with the expected id
    coVerify { movieDataSource.getKnownForPerson(id) }
  }

  @Test
  fun getKnownForPerson_returnsLoadingState() = runTest {
    // Mock the MovieDataSource's getKnownForPerson method to emit only loading
    coEvery { movieDataSource.getKnownForPerson(id) } returns flow {
      emit(NetworkResult.Loading) // Emit only Loading
    }

    // Act
    val result = repository.getKnownForPerson(id).first()

    // Assert
    assert(result is Outcome.Loading) // Ensure the first emission is Loading

    // Verify that getKnownForPerson was called exactly once with the expected id
    coVerify { movieDataSource.getKnownForPerson(id) }
  }

  @Test
  fun getImagePerson_returnsSuccessResult() = runTest {
    // Arrange
    val imagePersonResponse = mockk<ImagePersonResponse>(relaxed = true) // Mock response
    val networkResult = NetworkResult.Success(imagePersonResponse)

    // Mock the MovieDataSource's getImagePerson method
    coEvery { movieDataSource.getImagePerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock success result
    }

    // Act
    val result = repository.getImagePerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Success) // Check if the second emission is Success
    val successResult = result[1] as Outcome.Success
    assertEquals(
      imagePersonResponse.toImagePerson(),
      successResult.data
    ) // Verify that the data is correct

    // Verify that getImagePerson was called exactly once with the expected id
    coVerify { movieDataSource.getImagePerson(id) }
  }

  @Test
  fun getImagePerson_returnsErrorResult() = runTest {
    // Arrange
    val networkResult = NetworkResult.Error(errorMessage)

    // Mock the MovieDataSource's getImagePerson method
    coEvery { movieDataSource.getImagePerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock error result
    }

    // Act
    val result = repository.getImagePerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Error) // Check if the second emission is Error
    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message) // Verify the error message is correct

    // Verify that getImagePerson was called exactly once with the expected id
    coVerify { movieDataSource.getImagePerson(id) }
  }

  @Test
  fun getImagePerson_returnsLoadingState() = runTest {
    // Mock the MovieDataSource's getImagePerson method to emit only loading
    coEvery { movieDataSource.getImagePerson(id) } returns flow {
      emit(NetworkResult.Loading) // Emit only Loading
    }

    // Act
    val result = repository.getImagePerson(id).first()

    // Assert
    assert(result is Outcome.Loading) // Ensure the first emission is Loading

    // Verify that getImagePerson was called exactly once with the expected id
    coVerify { movieDataSource.getImagePerson(id) }
  }

  @Test
  fun getExternalIDPerson_returnsSuccessResult() = runTest {
    // Arrange
    val externalIDPersonResponse = mockk<ExternalIDPersonResponse>(relaxed = true) // Mock response
    val networkResult = NetworkResult.Success(externalIDPersonResponse)

    // Mock the MovieDataSource's getExternalIDPerson method
    coEvery { movieDataSource.getExternalIDPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock success result
    }

    // Act
    val result = repository.getExternalIDPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Success) // Check if the second emission is Success
    val successResult = result[1] as Outcome.Success
    assertEquals(
      externalIDPersonResponse.toExternalIDPerson(),
      successResult.data
    ) // Verify that the data is correct

    // Verify that getExternalIDPerson was called exactly once with the expected id
    coVerify { movieDataSource.getExternalIDPerson(id) }
  }

  @Test
  fun getExternalIDPerson_returnsErrorResult() = runTest {
    // Arrange
    val networkResult = NetworkResult.Error(errorMessage)

    // Mock the MovieDataSource's getExternalIDPerson method
    coEvery { movieDataSource.getExternalIDPerson(id) } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult) // Return the mock error result
    }

    // Act
    val result = repository.getExternalIDPerson(id).toList()

    // Assert
    assert(result[1] is Outcome.Error) // Check if the second emission is Error
    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message) // Verify the error message is correct

    // Verify that getExternalIDPerson was called exactly once with the expected id
    coVerify { movieDataSource.getExternalIDPerson(id) }
  }

  @Test
  fun getExternalIDPerson_returnsLoadingState() = runTest {
    // Mock the MovieDataSource's getExternalIDPerson method to emit only loading
    coEvery { movieDataSource.getExternalIDPerson(id) } returns flow {
      emit(NetworkResult.Loading) // Emit only Loading
    }

    // Act
    val result = repository.getExternalIDPerson(id).first()

    // Assert
    assert(result is Outcome.Loading) // Ensure the first emission is Loading

    // Verify that getExternalIDPerson was called exactly once with the expected id
    coVerify { movieDataSource.getExternalIDPerson(id) }
  }
}
