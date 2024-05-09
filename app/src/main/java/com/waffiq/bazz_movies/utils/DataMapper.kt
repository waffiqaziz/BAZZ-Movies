package com.waffiq.bazz_movies.utils

import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.domain.model.Favorite

object DataMapper {

  private fun mapResponsesToDomainFavorite(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    input: ResultItem
  ): Favorite {
    return Favorite(
      id = 0,
      mediaId = input.id ?: error("No ID for FavoriteEntity"),
      mediaType = input.mediaType ?: error("No Media Type for FavoriteEntity"),
      title = input.name ?: input.originalName ?: input.title ?: input.originalTitle ?: "N/A",
      releaseDate = input.releaseDate ?: input.firstAirDate ?: "N/A",
      rating = input.voteAverage ?: 0.0f,
      backDrop = input.backdropPath ?: "N/A",
      poster = input.posterPath ?: "N/A",
      genre = Helper.iterateGenre(input.genreIds ?: listOf()),
      popularity = input.popularity ?: 0.0,
      overview = input.overview ?: "N/A",
      isFavorite = isFavorite,
      isWatchlist = isWatchlist
    )
  }

  fun favTrueWatchlistTrue(data: ResultItem): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = true, isWatchlist = true, input = data)
  }

  fun favTrueWatchlistFalse(data: ResultItem): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = true, isWatchlist = false, input = data)
  }

  fun favFalseWatchlistTrue(data: ResultItem): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = false, isWatchlist = true, input = data)
  }

  fun favFalseWatchlistFalse(data: ResultItem): Favorite {
    return mapResponsesToDomainFavorite(isFavorite = false, isWatchlist = false, input = data)
  }

  fun mapEntitiesToDomainFavorite(data: List<FavoriteEntity>): List<Favorite> =
    data.map {
      Favorite(
        id = it.id,
        mediaId = it.mediaId,
        mediaType = it.mediaType,
        genre = it.genre,
        backDrop = it.backDrop,
        poster = it.poster,
        overview = it.overview,
        title = it.title,
        releaseDate = it.releaseDate,
        popularity = it.popularity,
        rating = it.rating,
        isFavorite = it.isFavorite,
        isWatchlist = it.isWatchlist,
      )
    }

  fun mapDomainToEntityFavorite(data: Favorite) = FavoriteEntity(
    id = data.id,
    mediaId = data.mediaId,
    mediaType = data.mediaType,
    genre = data.genre,
    backDrop = data.backDrop,
    poster = data.poster,
    overview = data.overview,
    title = data.title,
    releaseDate = data.releaseDate,
    popularity = data.popularity,
    rating = data.rating,
    isFavorite = data.isFavorite,
    isWatchlist = data.isWatchlist,
  )
}