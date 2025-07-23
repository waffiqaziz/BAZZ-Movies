package com.waffiq.bazz_movies.feature.detail.ui.manager

import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailMovieDataManagerTest {

  private lateinit var manager: DetailMovieDataManager
  private lateinit var activity: ComponentActivity

  private val detailViewModel: MediaDetailViewModel = mockk(relaxed = true)
  private val prefViewModel: DetailUserPrefViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
    manager = DetailMovieDataManager(detailViewModel, prefViewModel, dataMediaItem, activity)
  }

  @Test
  fun loadAllData_whenMovie_callsMovieDetailFunctions() {
    val regionLiveData = MutableLiveData("US")
    manager = DetailMovieDataManager(detailViewModel, prefViewModel, dataMediaItem, activity)

    every { prefViewModel.getUserRegion() } returns regionLiveData

    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify { detailViewModel.getMovieRecommendation(1234) }
    verify { detailViewModel.getMovieCredits(1234) }
    verify { detailViewModel.getMovieVideoLink(1234) }
    verify { detailViewModel.getMovieDetail(1234, "US") }
    verify { detailViewModel.getMovieWatchProviders("US", 1234) }
  }

  @Test
  fun loadAllData_whenTv_callsMovieDetailFunctions() {
    val regionLiveData = MutableLiveData("US")
    manager = DetailMovieDataManager(
      detailViewModel,
      prefViewModel,
      dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
      activity
    )
    every { prefViewModel.getUserRegion() } returns regionLiveData

    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234, "US") }
    verify { detailViewModel.getTvWatchProviders("US", 1234) }
  }

  @Test
  fun loadAllData_withImdbId_callsOMDbDetails() {
    val regionLiveData = MutableLiveData("US")
    val externalTvID = TvExternalIds(imdbId = "tt9999999")
    val tvExternalIdLiveData = MutableLiveData(externalTvID)

    every { prefViewModel.getUserRegion() } returns regionLiveData
    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailMovieDataManager(
      detailViewModel,
      prefViewModel,
      dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
      activity
    )

    manager.loadAllData()

    tvExternalIdLiveData.postValue(TvExternalIds(imdbId = "tt9999999"))
    regionLiveData.postValue("US")

    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234, "US") }
    verify { detailViewModel.getTvWatchProviders("US", 1234) }
    verify { detailViewModel.getOMDbDetails("tt9999999") }
  }

  @Test
  fun loadAllData_withNullImdbId_notCallsOMDbDetails() {
    val regionLiveData = MutableLiveData("US")
    val externalTvID = TvExternalIds(imdbId = null)
    val tvExternalIdLiveData = MutableLiveData(externalTvID)

    every { prefViewModel.getUserRegion() } returns regionLiveData
    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailMovieDataManager(
      detailViewModel,
      prefViewModel,
      dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
      activity
    )

    manager.loadAllData()
    regionLiveData.postValue("US")

    // verifications
    verify { detailViewModel.getTvRecommendation(1234) }
    verify { detailViewModel.getTvCredits(1234) }
    verify { detailViewModel.getTvTrailerLink(1234) }
    verify { detailViewModel.getTvDetail(1234, "US") }
    verify { detailViewModel.getTvWatchProviders("US", 1234) }
    verify(exactly = 0) { detailViewModel.getOMDbDetails("tt9999999") }
  }

  @Test
  fun loadAllData_withTvExternalIdIsNull_doesNotCallOMDb() {
    val regionLiveData = MutableLiveData("US")
    val tvExternalIdLiveData = MutableLiveData<TvExternalIds>()

    every { prefViewModel.getUserRegion() } returns regionLiveData
    every { detailViewModel.tvExternalID } returns tvExternalIdLiveData

    manager = DetailMovieDataManager(
      detailViewModel,
      prefViewModel,
      dataMediaItem.copy(mediaType = TV_MEDIA_TYPE),
      activity
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
    manager = DetailMovieDataManager(
      detailViewModel,
      prefViewModel,
      dataMediaItem.copy(mediaType = "person"),
      activity
    )
    every { prefViewModel.getUserRegion() } returns regionLiveData

    manager.loadAllData()

    // trigger LiveData emission manually
    regionLiveData.postValue("US")

    verify(exactly = 0) { detailViewModel.getMovieRecommendation(1234) }
    verify(exactly = 0) { detailViewModel.getMovieCredits(1234) }
    verify(exactly = 0) { detailViewModel.getMovieVideoLink(1234) }
    verify(exactly = 0) { detailViewModel.getMovieDetail(1234, "US") }
    verify(exactly = 0) { detailViewModel.getMovieWatchProviders("US", 1234) }
  }
}
