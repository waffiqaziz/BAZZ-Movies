package com.waffiq.bazz_movies.feature.detail.testutils

import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseInteractorTest {

  protected val mockRepository: IDetailRepository = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  protected abstract fun initInteractor()

  @Before
  fun baseSetUp() {
    initInteractor()
  }
}
