package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItemValue
import com.waffiq.bazz_movies.feature.detail.domain.model.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItemValue
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toGenresItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toSpokenLanguagesItem

object MovieMapper {

  fun DetailMovieResponse.toDetailMovie() = MovieDetail(
    originalLanguage = originalLanguage,
    imdbId = imdbId,
    video = video,
    title = title,
    backdropPath = backdropPath,
    revenue = revenue,
    listGenres = listGenresItemResponse?.map { it?.toGenresItem() },
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
    listReleaseDatesItem = listReleaseDatesResponseItem?.map { it?.toReleaseDatesItem() }
  )

  private fun ReleaseDatesResponseItem.toReleaseDatesItem() = ReleaseDatesItem(
    iso31661 = iso31661,
    listReleaseDatesItemValue = listReleaseDateResponseItemValue?.map { it.toReleaseDatesItemValue() }
  )

  private fun ReleaseDatesResponseItemValue.toReleaseDatesItemValue() = ReleaseDatesItemValue(
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
