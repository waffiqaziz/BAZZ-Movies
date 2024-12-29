package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesItemValueResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toSpokenLanguagesItem

object MovieMapper {

  fun DetailMovieResponse.toDetailMovie() = DetailMovie(
    originalLanguage = originalLanguage,
    imdbId = imdbId,
    video = video,
    title = title,
    backdropPath = backdropPath,
    revenue = revenue,
    listGenres = listGenresItemResponse?.map { it?.toGenresItem() ?: GenresItem() },
    popularity = popularity,
    releaseDates = releaseDatesResponse?.toReleaseDates(),
    listProductionCountriesItem = listProductionCountriesItemResponse?.map { it?.toProductionCountriesItem() },
    id = id,
    voteCount = voteCount,
    budget = budget,
    overview = overview,
    originalTitle = originalTitle,
    runtime = runtime,
    posterPath = posterPath,
    listSpokenLanguagesItem = listSpokenLanguagesItemResponse?.map { it?.toSpokenLanguagesItem() },
    listProductionCompaniesItem = listProductionCompaniesItemResponse?.map { it?.toProductionCompaniesItem() },
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    belongsToCollection = belongsToCollectionResponse?.toBelongsToCollection(),
    tagline = tagline,
    adult = adult,
    homepage = homepage,
    status = status
  )

  private fun ReleaseDatesResponse.toReleaseDates() = ReleaseDates(
    listReleaseDatesItem = listReleaseDatesItemResponse?.map { it?.toReleaseDatesItem() }
  )

  private fun ReleaseDatesItemResponse.toReleaseDatesItem() = ReleaseDatesItem(
    iso31661 = iso31661,
    listReleaseDatesitemValue = listReleaseDateItemValueResponse?.map { it.toReleaseDatesItemValue() }
  )

  private fun ReleaseDatesItemValueResponse.toReleaseDatesItemValue() = ReleaseDatesItemValue(
    descriptors = descriptors,
    note = note,
    type = type,
    iso6391 = iso6391,
    certification = certification,
    releaseDate = releaseDate
  )

  private fun BelongsToCollectionResponse.toBelongsToCollection() = BelongsToCollection(
    backdropPath = backdropPath,
    name = name,
    id = id,
    posterPath = posterPath
  )
}
