package com.waffiq.bazz_movies.core.network.data.remote.datasource.country

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.countryIPResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response.success

class CountryRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getCountryCode_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        mockApiResponse = success(countryIPResponseDump),
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
        expectedData = countryIPResponseDump,
      )
    }

  @Test
  fun getCountryCode_whenError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getCountryCode EDGE CASE
  @Test
  fun getCountryCode_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }

  @Test
  fun getCountryCode_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }

  @Test
  fun getCountryCode_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }

  @Test
  fun getCountryCode_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }

  @Test
  fun getCountryCode_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }

  @Test
  fun getCountryCode_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockCountryIPApiService.getIP() },
        dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      )
    }
  // endregion getCountryCode EDGE CASE
}
