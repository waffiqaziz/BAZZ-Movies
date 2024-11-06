package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.utils.common.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.utils.helpers.GenreHelper.getGenreName

object DatabaseMapper {

  private fun ResultItem.toFavorite(
    isFavorite: Boolean,
    isWatchlist: Boolean
  ) = Favorite(
    id = 0,
    mediaId = id,
    mediaType = mediaType,
    title = name ?: originalName ?: title ?: originalTitle ?: NOT_AVAILABLE,
    releaseDate = releaseDate ?: firstAirDate ?: NOT_AVAILABLE,
    rating = voteAverage ?: 0.0f,
    backDrop = backdropPath ?: NOT_AVAILABLE,
    poster = posterPath ?: NOT_AVAILABLE,
    genre = getGenreName(listGenreIds ?: listOf()),
    popularity = popularity ?: 0.0,
    overview = overview ?: NOT_AVAILABLE,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist
  )

  fun favTrueWatchlistTrue(data: ResultItem): Favorite {
    return data.toFavorite(isFavorite = true, isWatchlist = true)
  }

  fun favTrueWatchlistFalse(data: ResultItem): Favorite {
    return data.toFavorite(isFavorite = true, isWatchlist = false)
  }

  fun favFalseWatchlistTrue(data: ResultItem): Favorite {
    return data.toFavorite(isFavorite = false, isWatchlist = true)
  }

  fun FavoriteEntity.toFavorite() = Favorite(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    genre = genre,
    backDrop = backDrop,
    poster = poster,
    overview = overview,
    title = title,
    releaseDate = releaseDate,
    popularity = popularity,
    rating = rating,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist,
  )

  fun Favorite.toFavoriteEntity() = FavoriteEntity(
    id = id,
    mediaId = mediaId,
    mediaType = mediaType,
    genre = genre,
    backDrop = backDrop,
    poster = poster,
    overview = overview,
    title = title,
    releaseDate = releaseDate,
    popularity = popularity,
    rating = rating,
    isFavorite = isFavorite,
    isWatchlist = isWatchlist,
  )
}
