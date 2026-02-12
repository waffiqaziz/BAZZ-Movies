package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.local

import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.TestData
import junit.framework.TestCase
import org.junit.Test

class DiffCallbackDBTest {

  private val favorite = TestData().baseFavorite
  private val diffCallback = MediaAdapterDBHelper.FavoriteDiffCallback()

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem: Favorite = favorite
    val newItem: Favorite = favorite // same content

    TestCase.assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenContentIsDifferent_returnsFalse() {
    val oldItem = favorite
    val newItem = Favorite(
      id = 4535,
      mediaId = 34215,
      mediaType = "tv",
      genre = "Romance",
      backDrop = "backdrop.jpg",
      poster = "poster.jpg",
      overview = "Lorem ipsum",
      title = "Indonesian Series",
      releaseDate = "2025-01-02",
      popularity = 3124.0,
      rating = 10.0f,
      isFavorite = false,
      isWatchlist = false
    ) // different content

    TestCase.assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDifferentContent_returnsFalse() {
    val testCases = listOf(
      Pair(favorite, favorite.copy(isFavorite = !favorite.isFavorite)),
      Pair(favorite, favorite.copy(isWatchlist = !favorite.isWatchlist)),
      Pair(
        favorite,
        favorite.copy(mediaType = if (favorite.mediaType == "movie") "tv" else "movie")
      )
    )

    testCases.forEach { (oldItem, newItem) ->
      TestCase.assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
    }
  }

  @Test
  fun areItemsTheSame_whenSameContent_returnsTrue() {
    val testCases = listOf(
      Pair(favorite, favorite.copy(isFavorite = !favorite.isFavorite)),
      Pair(favorite, favorite.copy(isWatchlist = !favorite.isWatchlist)),
      Pair(
        favorite,
        favorite.copy(mediaType = if (favorite.mediaType == "movie") "tv" else "movie")
      )
    )

    testCases.forEach { (oldItem, newItem) ->
      TestCase.assertFalse(diffCallback.areItemsTheSame(oldItem, newItem))
    }

    TestCase.assertTrue(diffCallback.areItemsTheSame(favorite, favorite))
  }
}