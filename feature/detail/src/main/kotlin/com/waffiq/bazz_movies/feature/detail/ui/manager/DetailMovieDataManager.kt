package com.waffiq.bazz_movies.feature.detail.ui.manager

import androidx.lifecycle.LifecycleOwner
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel

/**
 * Manages the loading of detailed movie or TV show data for the detail screen.
 *
 * This class is responsible for orchestrating multiple data-fetching operations,
 * including details, credits, recommendations, external IDs, scores, and watch
 * providers, depending on whether the item is a movie or TV show.
 *
 * @param detailViewModel The ViewModel that provides methods for retrieving movie/TV details.
 * @param prefViewModel The ViewModel used to retrieve user preferences such as region.
 * @param dataExtra The [MediaItem] representing the movie or TV show being viewed.
 * @param lifecycleOwner The lifecycle owner used to observe LiveData from ViewModels.
 */
class DetailMovieDataManager(
  private val detailViewModel: MediaDetailViewModel,
  private val prefViewModel: DetailUserPrefViewModel,
  private var dataExtra: MediaItem,
  private val lifecycleOwner: LifecycleOwner,
) {

  /**
   * Loads all data required for displaying the detail screen.
   *
   * This includes:
   * - Recommendations based on the current movie/TV-series
   * - Main details, credits, watch providers, external IDs, and scores,
   *   depending on whether the item is a movie or TV show.
   */
  fun loadAllData() {
    loadRecommendations()

    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> loadMovieData()
      TV_MEDIA_TYPE -> loadTvData()
    }
  }

  /**
   * Loads movie or TV show recommendations based on the media type.
   */
  fun loadRecommendations() {
    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> detailViewModel.getMovieRecommendation(dataExtra.id)
      TV_MEDIA_TYPE -> detailViewModel.getTvRecommendation(dataExtra.id)
    }
  }

  /**
   * Loads detailed data specific to a movie, including:
   * - Credits (cast and crew)
   * - Movie details
   * - Trailer
   * - Watch providers based on the user's region
   */
  private fun loadMovieData() {
    detailViewModel.getMovieCredits(dataExtra.id)
    detailViewModel.getMovieVideoLink(dataExtra.id)
    prefViewModel.getUserRegion().observe(lifecycleOwner) { region ->
      detailViewModel.getMovieDetail(dataExtra.id, region)
      detailViewModel.getMovieWatchProviders(region.uppercase(), dataExtra.id)
    }
  }

  /**
   * Loads detailed data specific to a TV-series, including:
   * - External IMDb ID (not included by default in TV show results)
   * - OMDb score via IMDb ID
   * - Credits (cast and crew)
   * - TV details
   * - Trailer
   * - Watch providers based on the user's region
   */
  private fun loadTvData() {
    detailViewModel.getTvExternalIds(dataExtra.id)
    detailViewModel.tvExternalID.observe(lifecycleOwner) {
      if (it.imdbId != null) detailViewModel.getOMDbDetails(it.imdbId)
    }
    detailViewModel.getTvCredits(dataExtra.id)
    detailViewModel.getTvTrailerLink(dataExtra.id)
    prefViewModel.getUserRegion().observe(lifecycleOwner) { region ->
      detailViewModel.getTvDetail(dataExtra.id, region)
      detailViewModel.getTvWatchProviders(region.uppercase(), dataExtra.id)
    }
  }
}
