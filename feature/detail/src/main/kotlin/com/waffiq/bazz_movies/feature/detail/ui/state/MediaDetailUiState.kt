package com.waffiq.bazz_movies.feature.detail.ui.state

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails

data class MediaDetailUiState(
  // media data
  val detail: MediaDetail? = null,
  val credits: MediaCredits? = null,
  val omdbDetails: OMDbDetails? = null,
  val videoLink: String? = null,
  val watchProviders: WatchProvidersUiState = WatchProvidersUiState.Loading,
  val itemState: MediaState? = null,

  // state
  val isFavorite: Boolean = false,
  val isWatchlist: Boolean = false,
  val mediaStateResult: UpdateMediaStateResult? = null,

  // initial loading is true, cuz when load the page should be start on loading state
  val isLoading: Boolean = true,
)
