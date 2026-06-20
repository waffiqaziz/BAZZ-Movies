package com.waffiq.bazz_movies.feature.detail.ui.manager

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DetailMovieDataManagerTest {

  private lateinit var manager: DetailDataManager

  private val detailViewModel: MediaDetailViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    createManager()
  }

  @Test
  fun loadAllData_whenMovie_callsMovieDetailFunctions() {
    manager.loadAllData()

    verifyMovieCalls()
  }

  @Test
  fun loadAllData_whenTv_callsTvDetailFunctions() {
    createManager(TV_MEDIA_TYPE)

    manager.loadAllData()

    verifyTvCalls()
  }

  @Test
  fun loadAllData_withImdbId_callsTvAllScore() {
    createManager(TV_MEDIA_TYPE)

    manager.loadAllData()

    verifyTvCalls()
    verify { detailViewModel.getTvAllScore(1234) }
  }

  @Test
  fun loadAllData_withNullImdbId_notCallsOMDbDetails() {
    createManager(TV_MEDIA_TYPE)

    manager.loadAllData()

    verifyTvCalls()
    verify(exactly = 0) { detailViewModel.getOMDbDetails(any()) }
  }

  @Test
  fun loadAllData_withTvExternalIdIsNull_doesNotCallOMDb() {
    createManager(TV_MEDIA_TYPE)

    manager.loadAllData()

    verify(exactly = 0) { detailViewModel.getOMDbDetails(any()) }
  }

  @Test
  fun loadAllData_whenMediaTypePerson_doesNotCallMovieFunctions() {
    createManager("person")

    manager.loadAllData()

    verifyMovieCalls(exactly = 0)
  }

  private fun createManager(mediaType: String = MOVIE_MEDIA_TYPE) {
    manager = DetailDataManager(
      detailViewModel,
      dataMediaItem.copy(mediaType = mediaType),
    )
  }

  private fun verifyMovieCalls(exactly: Int = 1) {
    verify(exactly = exactly) { detailViewModel.getMovieRecommendation(any()) }
    verify(exactly = exactly) { detailViewModel.getMovieCredits(any()) }
    verify(exactly = exactly) { detailViewModel.getMovieDetail(any()) }
    verify(exactly = exactly) { detailViewModel.getMovieWatchProviders(any()) }
  }

  private fun verifyTvCalls(exactly: Int = 1) {
    verify(exactly = exactly) { detailViewModel.getTvRecommendation(any()) }
    verify(exactly = exactly) { detailViewModel.getTvCredits(any()) }
    verify(exactly = exactly) { detailViewModel.getTvDetail(any()) }
    verify(exactly = exactly) { detailViewModel.getTvWatchProviders(any()) }
  }
}
