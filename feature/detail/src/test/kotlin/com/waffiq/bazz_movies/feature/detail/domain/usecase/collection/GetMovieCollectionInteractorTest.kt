package com.waffiq.bazz_movies.feature.detail.domain.usecase.collection

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.COLLECTION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailCollection
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertIs

class GetMovieCollectionInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMovieCollectionInteractor

  override fun initInteractor() {
    interactor = GetMovieCollectionInteractor(mockDetailRepository)
  }

  @Test
  fun getMovieDetailWithUserRegion_whenSuccessful_emitsSuccess() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getMovieCollection(COLLECTION_ID) },
        mockResponse = detailCollection,
        interactorCall = { interactor.getMovieCollection(COLLECTION_ID) },
      ) { emission ->
        val mediaDetail = assertIs<DetailCollections>(emission.data)
        assertEquals(COLLECTION_ID, mediaDetail.id)
      }
    }
}
