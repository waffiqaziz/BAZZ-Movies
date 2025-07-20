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
class MediaCastItemTest {

  private val mediaCastItemValid = MediaCastItem(
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
  private val mediaCastItemNull = MediaCastItem()

  @Test
  fun mediaCastItem_withValidValue_returnsCorrectData() {
    assertEquals(10, mediaCastItemValid.castId)
    assertEquals("John Doe", mediaCastItemValid.character)
    assertEquals(2, mediaCastItemValid.gender)
    assertEquals("abc123", mediaCastItemValid.creditId)
    assertEquals("Acting", mediaCastItemValid.knownForDepartment)
    assertEquals("Jonathan Doe", mediaCastItemValid.originalName)
    assertEquals(12.5, mediaCastItemValid.popularity)
    assertEquals("John D.", mediaCastItemValid.name)
    assertEquals("/profile.jpg", mediaCastItemValid.profilePath)
    assertEquals(200, mediaCastItemValid.id)
    assertFalse(mediaCastItemValid.adult == true)
    assertEquals(1, mediaCastItemValid.order)
  }

  @Test
  fun mediaCastItem_withNullValue_returnsNull() {
    assertNull(mediaCastItemNull.castId)
    assertNull(mediaCastItemNull.character)
    assertNull(mediaCastItemNull.gender)
    assertNull(mediaCastItemNull.creditId)
    assertNull(mediaCastItemNull.knownForDepartment)
    assertNull(mediaCastItemNull.originalName)
    assertNull(mediaCastItemNull.popularity)
    assertNull(mediaCastItemNull.name)
    assertNull(mediaCastItemNull.profilePath)
    assertNull(mediaCastItemNull.id)
    assertNull(mediaCastItemNull.adult)
    assertNull(mediaCastItemNull.order)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      MediaCastItem::class.java.getField("CREATOR")
        .get(null) as Parcelable.Creator<MediaCastItem>
    val parcel = Parcel.obtain()
    mediaCastItemValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(mediaCastItemValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      MediaCastItem::class.java.getField("CREATOR").get(null) as Parcelable.Creator<MediaCastItem>
    val parcel = Parcel.obtain()
    mediaCastItemNull.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(mediaCastItemNull, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    mediaCastItemValid.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithNullValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    mediaCastItemNull.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = mediaCastItemValid.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
