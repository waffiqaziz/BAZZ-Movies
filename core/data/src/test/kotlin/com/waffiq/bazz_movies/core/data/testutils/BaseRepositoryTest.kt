package com.waffiq.bazz_movies.core.data.testutils

import com.waffiq.bazz_movies.core.data.testutils.TestVariables.createSampleMediaItemResponse
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.createSamplePagingData
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import org.junit.Rule

abstract class BaseRepositoryTest {

  protected val id = 123
  protected val region = "id"
  protected val response = createSampleMediaItemResponse()
  protected val fakePagingData = createSamplePagingData(response, response)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
}
