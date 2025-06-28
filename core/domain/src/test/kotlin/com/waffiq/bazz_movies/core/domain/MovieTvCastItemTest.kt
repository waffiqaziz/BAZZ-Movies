package com.waffiq.bazz_movies.core.domain

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MovieTvCastItemTest {

  private val movieCastItemValid = MovieTvCastItem(
    castId = 10,
    character = "John Doe",
    gender = 2,
    creditId = "abc123",
    knownForDepartment = "Acting",
    originalName = "Jonathan Doe",
    popularity = 12.5,
    name = "John D.",
    profilePath = "/profile.jpg",
    id = 200,
    adult = false,
    order = 1
  )
  private val movieCastItemNull = MovieTvCastItem()

  @Test
  fun movieTvCastItem_withValidValue_returnsCorrectData() {
    assertEquals(10, movieCastItemValid.castId)
    assertEquals("John Doe", movieCastItemValid.character)
    assertEquals(2, movieCastItemValid.gender)
    assertEquals("abc123", movieCastItemValid.creditId)
    assertEquals("Acting", movieCastItemValid.knownForDepartment)
    assertEquals("Jonathan Doe", movieCastItemValid.originalName)
    assertEquals(12.5, movieCastItemValid.popularity)
    assertEquals("John D.", movieCastItemValid.name)
    assertEquals("/profile.jpg", movieCastItemValid.profilePath)
    assertEquals(200, movieCastItemValid.id)
    assertFalse(movieCastItemValid.adult == true)
    assertEquals(1, movieCastItemValid.order)
  }

  @Test
  fun movieTvCastItem_withNullValue_returnsNull() {
    assertNull(movieCastItemNull.castId)
    assertNull(movieCastItemNull.character)
    assertNull(movieCastItemNull.gender)
    assertNull(movieCastItemNull.creditId)
    assertNull(movieCastItemNull.knownForDepartment)
    assertNull(movieCastItemNull.originalName)
    assertNull(movieCastItemNull.popularity)
    assertNull(movieCastItemNull.name)
    assertNull(movieCastItemNull.profilePath)
    assertNull(movieCastItemNull.id)
    assertNull(movieCastItemNull.adult)
    assertNull(movieCastItemNull.order)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      MovieTvCastItem::class.java.getField("CREATOR")
        .get(null) as Parcelable.Creator<MovieTvCastItem>
    val parcel = Parcel.obtain()
    movieCastItemValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(movieCastItemValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      MovieTvCastItem::class.java.getField("CREATOR").get(null) as Parcelable.Creator<MovieTvCastItem>
    val parcel = Parcel.obtain()
    movieCastItemNull.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(movieCastItemNull, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    movieCastItemValid.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithNullValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    movieCastItemNull.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = movieCastItemValid.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
