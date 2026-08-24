package com.waffiq.bazz_movies.navigation

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ListArgsTest {

  val args = ListArgs(
    listType = ListType.TRENDING_WEEK,
    mediaType = MediaSource.Typed("movie"),
    title = "Action Movies",
    id = 28,
    backdrop = "/backdrop.jpg",
  )

  @Test
  fun constructor_whenOptionalValuesNotProvided_expectedDefaultValuesUsed() {
    val args = ListArgs(
      listType = ListType.TRENDING_WEEK,
      mediaType = MediaSource.Trending,
      title = "Trending Movies",
    )

    assertEquals(ListType.TRENDING_WEEK, args.listType)
    assertEquals(MediaSource.Trending, args.mediaType)
    assertEquals("Trending Movies", args.title)
    assertEquals(-1, args.id)
    assertEquals("", args.backdrop)
  }

  @Test
  fun constructor_whenAllValuesProvided_expectedValuesStored() {
    assertEquals(ListType.TRENDING_WEEK, args.listType)
    assertEquals(MediaSource.Typed("movie"), args.mediaType)
    assertEquals("Action Movies", args.title)
    assertEquals(28, args.id)
    assertEquals("/backdrop.jpg", args.backdrop)
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      ListArgs::class.java.getField("CREATOR").get(null) as Parcelable.Creator<ListArgs>
    val parcel = Parcel.obtain()
    args.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    TestCase.assertEquals(args, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    args.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = args.describeContents()
    assertEquals(0, contentsValue)
  }
}
