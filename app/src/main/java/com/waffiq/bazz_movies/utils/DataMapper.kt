package com.waffiq.bazz_movies.utils

import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem

object DataMapper {

  private fun mapResponsesToEntitiesFavoriteDB(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    input: ResultItem
  ): FavoriteDB {
    return FavoriteDB(
      mediaId = input.id ?: error("No ID For Database"),
      mediaType = input.mediaType,
      title = input.name ?: input.originalName ?: input.title ?: input.originalTitle,
      releaseDate = input.releaseDate ?: input.firstAirDate,
      rating = input.voteAverage,
      backDrop = input.backdropPath,
      poster = input.posterPath,
      genre = Helper.iterateGenre(input.genreIds ?: listOf()),
      popularity = input.popularity,
      overview = input.overview,
      isFavorite = isFavorite,
      isWatchlist = isWatchlist
    )
  }

  fun favTrueWatchlistTrue(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = true, isWatchlist = true, input = data)
  }

  fun favTrueWatchlistFalse(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = true, isWatchlist = false, input = data)
  }

  fun favFalseWatchlistTrue(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = false, isWatchlist = true, input = data)
  }

  fun favFalseWatchlistFalse(data: ResultItem): FavoriteDB {
    return mapResponsesToEntitiesFavoriteDB(isFavorite = false, isWatchlist = false, input = data)
  }
}