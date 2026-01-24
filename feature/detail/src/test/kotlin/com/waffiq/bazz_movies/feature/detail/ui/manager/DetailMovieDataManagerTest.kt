package com.waffiq.bazz_movies.feature.detail.ui.manager

import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DetailMovieDataManagerTest {

  private lateinit var manager: DetailDataManager

  private val detailViewModel: MediaDetailViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    manager = DetailDataManager(detailViewModel, dataMediaItem)
  }

  @Test
  fun loadAllData_whenMovie_callsMovieDetailFunctions() {
    val regionLiveData = MutableLiveData("US")
    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify { detailViewModel.getMovieRecommendation(1234) }
    verify { detailViewModel.getMovieCredits(1234) }
    verify { detailViewModel.getMovieVideoLink(1234) }
    verify { detailViewModel.getMovieDetail(1234) }
    verify { detailViewModel.getMovieWatchProviders(1234) }
  }

  @Test
  fun loadAllData_whenTv_callsMovieDetailFunctions() {
    val regionLiveData = MutableLiveData("US")
    manager = DetailDataManager(
      detailViewModel, dataMediaItem.copy(mediaType = TV_MEDIA_TYPE)
    )

    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234) }
    verify { detailViewModel.getTvWatchProviders(1234) }
  }

  @Test
  fun loadAllData_withImdbId_callsOMDbDetails() {
    val regionLiveData = MutableLiveData("US")
    val externalTvID = TvExternalIds(imdbId = "tt9999999")
    val tvExternalIdLiveData = MutableLiveData(externalTvID)

    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailDataManager(
      detailViewModel, dataMediaItem.copy(mediaType = TV_MEDIA_TYPE)
    )

    manager.loadAllData()

    tvExternalIdLiveData.postValue(TvExternalIds(imdbId = "tt9999999"))
    regionLiveData.postValue("US")

    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234) }
    verify { detailViewModel.getTvWatchProviders(1234) }
    verify { detailViewModel.getTvAllScore(1234) }
  }

  @Test
  fun loadAllData_withNullImdbId_notCallsOMDbDetails() {
    val regionLiveData = MutableLiveData("US")
    val externalTvID = TvExternalIds(imdbId = null)
    val tvExternalIdLiveData = MutableLiveData(externalTvID)

    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailDataManager(
      detailViewModel, dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
    )

    manager.loadAllData()
    regionLiveData.postValue("US")

    // verifications
    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234) }
    verify { detailViewModel.getTvWatchProviders(1234) }
    verify(exactly = 0) { detailViewModel.getOMDbDetails("tt9999999") }
  }

  @Test
  fun loadAllData_withTvExternalIdIsNull_doesNotCallOMDb() {
    val regionLiveData = MutableLiveData("US")
    val tvExternalIdLiveData = MutableLiveData<TvExternalIds>()

    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailDataManager(
      detailViewModel, dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
    )

    manager.loadAllData()

    // simulate null case
    tvExternalIdLiveData.postValue(null)
    regionLiveData.postValue("US")

    verify(exactly = 0) { detailViewModel.getOMDbDetails(any()) }
  }

  @Test
  fun loadAllData_whenMediaTypePerson_callsMovieDetailFunctions() {
    val regionLiveData = MutableLiveData("US")
    manager = DetailDataManager(
      detailViewModel, dataMediaItem.copy(mediaType = "person"),
    )

    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify(exactly = 0) { detailViewModel.getMovieRecommendation(1234) }
    verify(exactly = 0) { detailViewModel.getMovieCredits(1234) }
    verify(exactly = 0) { detailViewModel.getMovieVideoLink(1234) }
    verify(exactly = 0) { detailViewModel.getMovieDetail(1234) }
    verify(exactly = 0) { detailViewModel.getMovieWatchProviders(1234) }
  }
}
