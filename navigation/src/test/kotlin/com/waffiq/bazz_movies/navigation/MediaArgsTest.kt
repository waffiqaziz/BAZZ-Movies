package com.waffiq.bazz_movies.navigation

import android.os.Parcel
import android.os.Parcelable
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.navigation.testutils.DummyData.mediaArgs
import com.waffiq.bazz_movies.navigation.testutils.DummyData.mediaArgsNull
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaArgsTest {

  @Test
  fun mediaArgs_withValidValue_returnsCorrectData() {
    assertEquals(42, mediaArgs.id)
    assertEquals(MediaType.MOVIE, mediaArgs.mediaType)
    assertEquals("Test Movie", mediaArgs.name)
    assertEquals("Test Title", mediaArgs.title)
    assertEquals("Original Title", mediaArgs.originalTitle)
    assertEquals("Original Name", mediaArgs.originalName)
    assertEquals("Overview of the movie.", mediaArgs.overview)
    assertEquals("en", mediaArgs.originalLanguage)
    assertEquals(listOf(28, 12), mediaArgs.listGenreIds)
    assertEquals("/poster1.jpg", mediaArgs.posterPath)
    assertEquals("/backdrop1.jpg", mediaArgs.backdropPath)
    assertEquals("2025-01-01", mediaArgs.firstAirDate)
    assertEquals("2026-01-01", mediaArgs.releaseDate)
    assertEquals(true, mediaArgs.video)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      MediaArgs::class.java.getField("CREATOR").get(null) as Parcelable.Creator<MediaArgs>
    val parcel = Parcel.obtain()
    mediaArgs.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(mediaArgs, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      MediaArgs::class.java.getField("CREATOR").get(null) as Parcelable.Creator<MediaArgs>
    val parcel = Parcel.obtain()
    mediaArgsNull.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(mediaArgsNull, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    mediaArgs.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithNullValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    mediaArgsNull.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = mediaArgs.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
