package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState

data class MediaDetail(
  val id: Int,
  val imdbId: String? = null,
  val ageRating: String? = null,
  val credits: MediaCredits? = null,
  val duration: String? = null,
  val genre: String? = null,
  val genreId: List<Int>? = null,
  val keywords: List<MediaKeywordsItem?>? = null,
  val language: String? = null,
  val releaseDateRegion: ReleaseDateRegion,
  val status: String? = null,
  val tmdbScore: String? = null,
  val trailer: String? = null,
  val watchProviders: WatchProvidersUiState = WatchProvidersUiState.Loading,

  // movie
  val budget: String? = null,
  val revenue: String? = null,
  val belongsToCollection: BelongsToCollection? = null,

  // tv
  val totalEpisodes: Int? = null,
  val totalSeasons: Int? = null,

  // updated data
  val title: String = "",
  val releaseDate: String = "", // in format yyyy-mm-dd
  val popularity: Float = 0f,
  val backdrop: String = "",
  val poster: String = "",
  val overview: String = "",
)
