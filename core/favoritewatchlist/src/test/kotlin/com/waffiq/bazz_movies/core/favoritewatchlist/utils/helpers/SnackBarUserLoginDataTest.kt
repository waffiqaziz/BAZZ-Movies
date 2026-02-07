package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SnackBarUserLoginDataTest {

  @Test
  fun snackBarUserLoginData_withCorrectValues_createsTheInstance() {
    val favorite = FavoriteParams(mediaType = "movie", mediaId = 123, favorite = true)
    val watchlist = WatchlistParams(mediaType = "tv", mediaId = 456, watchlist = true)

    val data = SnackBarUserLoginData(
      isSuccess = true,
      title = "Login successful",
      favoriteModel = favorite,
      watchlistModel = watchlist
    )

    assertTrue(data.isSuccess)
    assertEquals("Login successful", data.title)
    assertEquals(favorite, data.favoriteModel)
    assertEquals(watchlist, data.watchlistModel)
  }

  @Test
  fun snackBarUserLoginData_whenContentsAreEqual_instancesAreEqual() {
    val favorite = FavoriteParams("movie", 123, true)
    val watchlist = WatchlistParams("tv", 456, true)

    val data1 = SnackBarUserLoginData(true, "Success", favorite, watchlist)
    val data2 = SnackBarUserLoginData(true, "Success", favorite, watchlist)

    assertEquals(data2, data1)
    assertEquals(data2.hashCode(), data1.hashCode())
  }

  @Test
  fun snackBarUserLoginData_whenCopyFunctionCalled_updatesSpecificFields() {
    val favorite = FavoriteParams("movie", 123, true)
    val original = SnackBarUserLoginData(true, "Success", favorite, null)

    val updated = original.copy(title = "Updated Title", isSuccess = false)

    assertFalse(updated.isSuccess)
    assertEquals("Updated Title", updated.title)
    assertEquals(favorite, updated.favoriteModel)
    assertNull(updated.watchlistModel)
  }

  @Test
  fun snackBarUserLoginData_withNullValues_shouldHandleCorrectly() {
    val data = SnackBarUserLoginData(
      isSuccess = false,
      title = "Failed",
      favoriteModel = null,
      watchlistModel = null
    )

    assertFalse(data.isSuccess)
    assertEquals("Failed", data.title)
    assertNull(data.favoriteModel)
    assertNull(data.watchlistModel)
  }
}
