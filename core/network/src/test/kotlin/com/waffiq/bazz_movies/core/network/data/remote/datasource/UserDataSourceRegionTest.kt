package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseUserDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.countryIPResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class UserDataSourceRegionTest : BaseUserDataSourceTest() {

  @Test
  fun getCountryCode_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      mockApiResponse = Response.success(countryIPResponseDump),
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
      expectedData = countryIPResponseDump,
    ) { data ->
      assertEquals("ID", data.country)
      assertEquals("103.187.242.255", data.ip)
    }
  }

  @Test
  fun getCountryCode_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region getCountryCode EDGE CASE
  @Test
  fun getCountryCode_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }
  // endregion getCountryCode EDGE CASE
}
