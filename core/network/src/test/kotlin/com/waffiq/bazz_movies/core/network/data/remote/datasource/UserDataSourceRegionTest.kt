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
  fun getCountryCode_returnExpectedResponse() = runTest {
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
  fun getCountryCode_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region getCountryCode EDGE CASE
  @Test
  fun getCountryCode_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }

  @Test
  fun getCountryCode_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { countryIPApiService.getIP() },
      dataSourceEndpointCall = { userDataSource.getCountryCode() },
    )
  }
  // endregion getCountryCode EDGE CASE
}
