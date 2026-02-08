package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.RatingsItem

object OMDbMapper {

  fun OMDbDetailsResponse.toOMDbDetails() =
    OMDbDetails(
      metascore = metascore,
      boxOffice = boxOffice,
      website = website,
      imdbRating = imdbRating,
      imdbVotes = imdbVotes,
      ratings = ratings?.map { it.toRatingsItem() },
      runtime = runtime,
      language = language,
      rated = rated,
      production = production,
      released = released,
      imdbID = imdbID,
      plot = plot,
      director = director,
      title = title,
      actors = actors,
      response = response,
      type = type,
      awards = awards,
      dVD = dVD,
      year = year,
      poster = poster,
      country = country,
      genre = genre,
      writer = writer,
    )

  private fun RatingsItemResponse.toRatingsItem() =
    RatingsItem(
      value = value,
      source = source,
    )
}
