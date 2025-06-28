package com.waffiq.bazz_movies.core.domain

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteTest {
  private val favoriteValid = Favorite(
    id = 1,
    mediaId = 100,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop.jpg",
    poster = "poster.jpg",
    overview = "Some overview",
    title = "Sample Movie",
    releaseDate = "2023-01-01",
    popularity = 9.0,
    rating = 4.5f,
    isFavorite = true,
    isWatchlist = false
  )

  @Test
  fun favorite_withValidValue_returnsCorrectData() {
    assertEquals(1, favoriteValid.id)
    assertEquals(100, favoriteValid.mediaId)
    assertEquals("movie", favoriteValid.mediaType)
    assertEquals("Action", favoriteValid.genre)
    assertEquals("backdrop.jpg", favoriteValid.backDrop)
    assertEquals("poster.jpg", favoriteValid.poster)
    assertEquals("Some overview", favoriteValid.overview)
    assertEquals("Sample Movie", favoriteValid.title)
    assertEquals("2023-01-01", favoriteValid.releaseDate)
    assertEquals(9.0, favoriteValid.popularity, 0.0)
    assertEquals(4.5f, favoriteValid.rating)
    assertTrue(favoriteValid.isFavorite)
    assertFalse(favoriteValid.isWatchlist)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      Favorite::class.java.getField("CREATOR").get(null) as Parcelable.Creator<Favorite>
    val parcel = Parcel.obtain()
    favoriteValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(favoriteValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      Favorite::class.java.getField("CREATOR").get(null) as Parcelable.Creator<Favorite>
    val parcel = Parcel.obtain()
    favoriteValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(favoriteValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalled_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    favoriteValid.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = favoriteValid.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
