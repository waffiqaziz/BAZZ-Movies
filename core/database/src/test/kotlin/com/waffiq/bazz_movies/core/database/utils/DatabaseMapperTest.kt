package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.toFavorite
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.toFavoriteEntity
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DatabaseMapperTest {
  private val genreName = "Action, Adventure"
  private val mediaItem1 = MediaItem(
    firstAirDate = "firstAirData",
    overview = "overview",
    originalLanguage = "originalLanguage",
    listGenreIds = listOf(28, 12),
    posterPath = "posterPath",
    backdropPath = "backdropPath",
    mediaType = "tv",
    originalName = "originalName",
    popularity = 90.0,
    voteAverage = 65f,
    name = "name",
    id = 436719,
    adult = false,
    voteCount = 43691,
    originalTitle = "originalTitle",
    video = false,
    title = "title",
    releaseDate = "releaseDate",
    originCountry = listOf("indonesian")
  )

  @Test
  fun toFavorite_withValidAllTrueValues_returnsFavorite() {

    // test case 1: valid data
    val favorite1 = mediaItem1.toFavorite(isFavorite = true, isWatchlist = true)
    assertEquals(favorite1.id, 0)
    assertEquals(favorite1.mediaId, 436719)
    assertEquals(favorite1.mediaType, "tv")
    assertEquals(favorite1.title, "name")
    assertEquals(favorite1.releaseDate, "releaseDate")
    assertEquals(favorite1.rating, 65f)
    assertEquals(favorite1.backDrop, "backdropPath")
    assertEquals(favorite1.poster, "posterPath")
    assertEquals(favorite1.genre, genreName)
    assertEquals("Different value", favorite1.popularity, 90.0, 0.0)
    assertEquals(favorite1.overview, "overview")
    assertEquals(favorite1.isFavorite, true)
    assertEquals(favorite1.isWatchlist, true)

    // test case 2: title using originalName, release date using firstAirDate, popularity null,
    // overView null, rating null
    val mediaItem2 = MediaItem(
      originalName = "originalName",
      firstAirDate = "firstAirDate",
      voteAverage = null,
      popularity = null
    )
    val favorite2 = mediaItem2.toFavorite(isFavorite = false, isWatchlist = false)
    assertEquals(favorite2.title, "originalName")
    assertEquals(favorite2.releaseDate, "firstAirDate")
    assertEquals(favorite2.rating, 0.0f)
    assertEquals("Different value", favorite2.popularity, 0.0, 0.0)
    assertEquals(favorite2.overview, "N/A")
    assertEquals(favorite2.genre, "")

    // test case 3: title using title, release date null
    val mediaItem3 = MediaItem(title = "title")
    val favorite3 = mediaItem3.toFavorite(isFavorite = false, isWatchlist = false)
    assertEquals(favorite3.title, "title")
    assertEquals(favorite3.releaseDate, "N/A")

    // test case 4: title using originalTitle
    val mediaItem4 = MediaItem(originalTitle = "originalTitle")
    val favorite4 = mediaItem4.toFavorite(isFavorite = false, isWatchlist = false)
    assertEquals(favorite4.title, "originalTitle")

    // test case 5: title null
    val mediaItem = MediaItem()
    val favorite5 = mediaItem.toFavorite(isFavorite = false, isWatchlist = false)
    assertEquals(favorite5.title, "N/A")
  }

  @Test
  fun favTrueWatchlistTrue_withValidValue_returnCorrectData() {
    val result = favTrueWatchlistTrue(mediaItem1)
    assertTrue(result.isFavorite)
    assertTrue(result.isWatchlist)
  }

  @Test
  fun favTrueWatchlistFalse_withValidValue_returnCorrectData() {
    val result = favTrueWatchlistFalse(mediaItem1)
    assertTrue(result.isFavorite)
    assertTrue(!result.isWatchlist)
  }

  @Test
  fun favFalseWatchlistTrue_withValidValue_returnCorrectData() {
    val result = favFalseWatchlistTrue(mediaItem1)
    assertTrue(!result.isFavorite)
    assertTrue(result.isWatchlist)
  }

  @Test
  fun toFavoriteEntity_withFavoriteIsTrue_ReturnsFavoriteEntity() {
    val favorite = Favorite(
      id = 1,
      mediaId = 1,
      mediaType = "movie",
      genre = genreName,
      backDrop = "backDrop",
      poster = "poster",
      overview = "overview",
      title = "title",
      releaseDate = "releaseDate",
      popularity = 1234.0,
      rating = 90.0f,
      isFavorite = false,
      isWatchlist = true
    )
    val favoriteEntity = favorite.toFavoriteEntity()
    assertEquals(favoriteEntity.id, 1)
    assertEquals(favoriteEntity.mediaId, 1)
    assertEquals(favoriteEntity.mediaType, "movie")
    assertEquals(favoriteEntity.genre, genreName)
    assertEquals(favoriteEntity.backDrop, "backDrop")
    assertEquals(favoriteEntity.poster, "poster")
    assertEquals(favoriteEntity.overview, "overview")
    assertEquals(favoriteEntity.title, "title")
    assertEquals(favoriteEntity.releaseDate, "releaseDate")
    assertEquals("Value not same", favoriteEntity.popularity, 1234.0, 0.0)
    assertEquals(favoriteEntity.rating, 90.0f)
    assertEquals(favoriteEntity.isFavorite, false)
    assertEquals(favoriteEntity.isWatchlist, true)
  }

  @Test
  fun toFavorite_withFavoriteIsFalse_returnsFavorite() {
    val favoriteEntity = FavoriteEntity(
      id = 2,
      mediaId = 2,
      mediaType = "tv",
      genre = genreName,
      backDrop = "backDrop",
      poster = "poster",
      overview = "overview",
      title = "title",
      releaseDate = "releaseDate",
      popularity = 65534.0,
      rating = 50.0f,
      isFavorite = true,
      isWatchlist = false
    )
    val favorite = favoriteEntity.toFavorite()
    assertEquals(favorite.id, 2)
    assertEquals(favorite.mediaId, 2)
    assertEquals(favorite.mediaType, "tv")
    assertEquals(favorite.genre, genreName)
    assertEquals(favorite.backDrop, "backDrop")
    assertEquals(favorite.poster, "poster")
    assertEquals(favorite.overview, "overview")
    assertEquals(favorite.title, "title")
    assertEquals(favorite.releaseDate, "releaseDate")
    assertEquals("Value not same", favorite.popularity, 65534.0, 0.0)
    assertEquals(favorite.rating, 50.0f)
    assertEquals(favorite.isFavorite, true)
    assertEquals(favorite.isWatchlist, false)
  }
}
