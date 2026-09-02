package com.waffiq.bazz_movies.navigation

import android.os.Parcel
import android.os.Parcelable
import com.waffiq.bazz_movies.navigation.testutils.DummyData.personArgs
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersonArgsTest {

  @Test
  fun personArgs_withValidValue_returnsCorrectData() {
    assertEquals(122333, personArgs.id)
    assertEquals("Name", personArgs.name)
    assertEquals("Name Original", personArgs.originalName)
    assertEquals("/profile.jpg", personArgs.profilePath)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      PersonArgs::class.java.getField("CREATOR").get(null) as Parcelable.Creator<PersonArgs>
    val parcel = Parcel.obtain()
    personArgs.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(personArgs, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValidValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    personArgs.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = personArgs.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
