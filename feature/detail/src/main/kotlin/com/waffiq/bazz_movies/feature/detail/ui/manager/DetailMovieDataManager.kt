package com.waffiq.bazz_movies.feature.detail.ui.manager

import androidx.lifecycle.LifecycleOwner
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailMovieViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel

class DetailMovieDataManager(
  private val detailViewModel: DetailMovieViewModel,
  private val prefViewModel: DetailUserPrefViewModel,
  private var dataExtra: ResultItem,
  private val lifecycleOwner: LifecycleOwner,
) {

  fun loadInitialData() {
    // load recommendations
    loadRecommendations()

    // load data based on media type
    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> loadMovieData()
      TV_MEDIA_TYPE -> loadTvData()
    }
  }

  fun loadRecommendations() {
    when (dataExtra.mediaType) {
      MOVIE_MEDIA_TYPE -> detailViewModel.getRecommendationMovie(dataExtra.id)
      TV_MEDIA_TYPE -> detailViewModel.getRecommendationTv(dataExtra.id)
    }
  }

  private fun loadMovieData() {
    // load movie credits (cast and crew)
    detailViewModel.getMovieCredits(dataExtra.id)

    // load movie details and watch providers based on user region
    prefViewModel.getUserRegion().observe(lifecycleOwner) { region ->
      detailViewModel.detailMovie(dataExtra.id, region)
      detailViewModel.getMovieWatchProviders(region.uppercase(), dataExtra.id)
    }
  }

  private fun loadTvData() {
    // load external ID (IMDb ID),
    // due to missing IMDb ID on default ResultItem for tv-series
    detailViewModel.getExternalTvId(dataExtra.id)

    // get OMDB score using IMDb ID from ExternalID
    detailViewModel.tvExternalID.observe(lifecycleOwner) {
      if (it?.imdbId != null) {
        detailViewModel.getScoreOMDb(it.imdbId)
      }
    }

    // load TV credits (cast and crew)
    detailViewModel.getTvCredits(dataExtra.id)

    // load TV video links
    detailViewModel.getLinkTv(dataExtra.id)

    // load TV details and watch providers based on user region
    prefViewModel.getUserRegion().observe(lifecycleOwner) { region ->
      detailViewModel.detailTv(dataExtra.id, region)
      detailViewModel.getTvWatchProviders(region.uppercase(), dataExtra.id)
    }
  }
}
