package com.waffiq.bazz_movies.core.domain

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResultItemTest {

  private val resultItemValid = ResultItem(
    firstAirDate = "2023-01-01",
    overview = "A thrilling movie about testing.",
    originalLanguage = "en",
    listGenreIds = listOf(28, 12),
    posterPath = "/poster.jpg",
    backdropPath = "/backdrop.jpg",
    mediaType = "movie",
    originalName = "Original Name",
    popularity = 123.45,
    voteAverage = 8.7f,
    name = "Test Movie",
    id = 42,
    adult = true,
    voteCount = 789,
    originalTitle = "Original Title",
    video = true,
    title = "Test Title",
    releaseDate = "2023-01-01",
    originCountry = listOf("US", "CA")
  )
  private val resultItemNull = ResultItem()

  @Test
  fun resultItem_withValidValue_returnsCorrectData() {
    assertEquals("2023-01-01", resultItemValid.firstAirDate)
    assertEquals("A thrilling movie about testing.", resultItemValid.overview)
    assertEquals("en", resultItemValid.originalLanguage)
    assertEquals(listOf(28, 12), resultItemValid.listGenreIds)
    assertEquals("/poster.jpg", resultItemValid.posterPath)
    assertEquals("/backdrop.jpg", resultItemValid.backdropPath)
    assertEquals("movie", resultItemValid.mediaType)
    assertEquals("Original Name", resultItemValid.originalName)
    assertEquals(123.45, resultItemValid.popularity)
    assertEquals(8.7f, resultItemValid.voteAverage)
    assertEquals("Test Movie", resultItemValid.name)
    assertEquals(42, resultItemValid.id)
    assertTrue(resultItemValid.adult)
    assertEquals(789, resultItemValid.voteCount)
    assertEquals("Original Title", resultItemValid.originalTitle)
    assertTrue(resultItemValid.video)
    assertEquals("Test Title", resultItemValid.title)
    assertEquals("2023-01-01", resultItemValid.releaseDate)
    assertEquals(listOf("US", "CA"), resultItemValid.originCountry)
  }

  @Test
  fun resultItem_withNullValue_returnsCorrectData() {
    assertNull(resultItemNull.firstAirDate)
    assertNull(resultItemNull.overview)
    assertNull(resultItemNull.originalLanguage)
    assertNull(resultItemNull.listGenreIds)
    assertNull(resultItemNull.posterPath)
    assertNull(resultItemNull.backdropPath)
    assertEquals("movie", resultItemNull.mediaType)
    assertNull(resultItemNull.originalName)
    assertEquals(0.0, resultItemNull.popularity)
    assertEquals(0f, resultItemNull.voteAverage)
    assertNull(resultItemNull.name)
    assertEquals(0, resultItemNull.id)
    assertFalse(resultItemNull.adult)
    assertEquals(0, resultItemNull.voteCount)
    assertNull(resultItemNull.originalTitle)
    assertFalse(resultItemNull.video)
    assertNull(resultItemNull.title)
    assertNull(resultItemNull.releaseDate)
    assertNull(resultItemNull.originCountry)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      ResultItem::class.java.getField("CREATOR").get(null) as Parcelable.Creator<ResultItem>
    val parcel = Parcel.obtain()
    resultItemValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(resultItemValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      ResultItem::class.java.getField("CREATOR").get(null) as Parcelable.Creator<ResultItem>
    val parcel = Parcel.obtain()
    resultItemNull.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(resultItemNull, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    resultItemValid.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithNullValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    resultItemNull.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = resultItemValid.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
