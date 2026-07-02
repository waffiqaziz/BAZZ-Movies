package com.waffiq.bazz_movies.feature.detail.ui.state

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem

data class CollectionUiState(
  val isLoading: Boolean = false,
  val isError: Boolean = false,

  // data ready for the UI
  val name: String = "",
  val overview: String = "",
  val genreIds: List<Int> = emptyList(),
  val backdropUrl: Any? = null, // could be link or drawable
  val parts: List<PartsItem> = emptyList(),
)
