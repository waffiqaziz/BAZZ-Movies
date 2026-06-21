package com.waffiq.bazz_movies.feature.detail.ui.manager

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel

/**
 * Manages the loading of detailed movie or TV show data for the detail screen.
 *
 * This class is responsible for orchestrating multiple data-fetching operations,
 * including details, credits, recommendations, external IDs, scores, and watch
 * providers, depending on whether the item is a movie or TV show.
 *
 * @param detailViewModel The ViewModel that provides methods for retrieving movie/TV details.
 * @param dataExtra The [MediaItem] representing the movie or TV show being viewed.
 */
class DetailDataManager(
  private val detailViewModel: MediaDetailViewModel,
  private var dataExtra: MediaItem,
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
    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> {
        detailViewModel.getMovieRecommendation(dataExtra.id)

        // Credits (cast and crew)
        // OMDb score via IMDb ID
        // Movie details
        // Trailer
        // Watch providers based on the user's region
        detailViewModel.getMovieDetail(dataExtra.id)
      }

      TV_MEDIA_TYPE -> {
        detailViewModel.getTvRecommendation(dataExtra.id)

        // - TV details
        // - External IMDb ID (not included by default in TV show results)
        // - OMDb score via IMDb ID
        // - Credits (cast and crew)
        // - Trailer
        // - Watch providers based on the user's region
        detailViewModel.getTvDetail(dataExtra.id)
      }
    }
  }
}
