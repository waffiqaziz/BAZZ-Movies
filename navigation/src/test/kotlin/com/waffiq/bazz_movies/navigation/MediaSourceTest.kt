package com.waffiq.bazz_movies.navigation

import android.os.Parcel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaSourceTest {

  @Test
  fun typeName_whenTrending_expectedTrendingString() {
    val result = MediaSource.Trending.typeName

    assertEquals("trending", result)
  }

  @Test
  fun typeName_whenTyped_expectedMediaTypeString() {
    val mediaType = "movie"

    val result = MediaSource.Typed(mediaType).typeName

    assertEquals(mediaType, result)
  }

  @Test
  fun trending_parcelable_roundTrip() {
    val source: MediaSource = MediaSource.Trending

    assertEquals(0, source.describeContents())

    val parcel = Parcel.obtain()
    source.writeToParcel(parcel, 0)
    parcel.recycle()
  }
}
