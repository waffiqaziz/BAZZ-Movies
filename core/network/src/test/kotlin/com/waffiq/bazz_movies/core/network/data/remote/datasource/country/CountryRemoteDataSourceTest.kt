package com.waffiq.bazz_movies.core.network.data.remote.datasource.country

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class CountryRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getCountryCode_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      mockApiResponse = Response.success(DataDumpManager.countryIPResponseDump),
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      expectedData = DataDumpManager.countryIPResponseDump,
    )
  }

  @Test
  fun getCountryCode_whenError_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getCountryCode EDGE CASE
  @Test
  fun getCountryCode_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockCountryIPApiService.getIP() },
      dataSourceEndpointCall = { countryRemoteDataSource.getCountryCode() },
    )
  }
  // endregion getCountryCode EDGE CASE
}