package com.waffiq.bazz_movies.feature.person.domain.model

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailPersonTest {

  @Test
  fun detailPerson_whenInstantiatedWithNoData_returnsAllFieldsNull() {
    val detailPerson = DetailPerson()

    assertNull(detailPerson.alsoKnownAs)
    assertNull(detailPerson.birthday)
    assertNull(detailPerson.gender)
    assertNull(detailPerson.imdbId)
    assertNull(detailPerson.knownForDepartment)
    assertNull(detailPerson.profilePath)
    assertNull(detailPerson.biography)
    assertNull(detailPerson.deathday)
    assertNull(detailPerson.placeOfBirth)
    assertNull(detailPerson.popularity)
    assertNull(detailPerson.name)
    assertNull(detailPerson.id)
    assertNull(detailPerson.adult)
    assertNull(detailPerson.homepage)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    val original = DetailPerson(
      alsoKnownAs = listOf("Name1", "Name2"),
      birthday = "1990-01-01",
      gender = 1,
      imdbId = "tt1234567",
      knownForDepartment = "Acting",
      profilePath = "/path/to/profile.jpg",
      biography = "biography",
      deathday = "2023-10-01",
      placeOfBirth = "Los Angeles",
      popularity = 85.6f,
      name = "Your Name",
      id = 12345,
      adult = false,
      homepage = "https://example.com"
    )

    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      DetailPerson::class.java.getField("CREATOR").get(null) as Parcelable.Creator<DetailPerson>
    val parcel = Parcel.obtain()
    original.writeToParcel(parcel, 0)

    // reset the parcel for reading
    parcel.setDataPosition(0)

    // create the object from the parcel
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(original, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    val original = DetailPerson(
      alsoKnownAs = null,
      birthday = null,
      gender = null,
      imdbId = null,
      knownForDepartment = null,
      profilePath = null,
      biography = null,
      deathday = null,
      placeOfBirth = null,
      popularity = null,
      name = null,
      id = null,
      adult = null,
      homepage = null
    )

    @Suppress("UNCHECKED_CAST")
    val creator =
      DetailPerson::class.java.getField("CREATOR").get(null) as Parcelable.Creator<DetailPerson>
    val parcel = Parcel.obtain()
    original.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(original, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAlsoKnownAsIsEmpty_readsAndWritesCorrectly() {
    val original = DetailPerson(alsoKnownAs = listOf())

    @Suppress("UNCHECKED_CAST")
    val creator =
      DetailPerson::class.java.getField("CREATOR").get(null) as Parcelable.Creator<DetailPerson>
    val parcel = Parcel.obtain()
    original.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(original, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalled_performsWriteWithoutCrash() {
    val detailPerson = DetailPerson(
      alsoKnownAs = listOf("Name1", "Name2"),
      birthday = "1990-01-01",
      gender = 1,
      imdbId = "tt1234567",
      knownForDepartment = "Acting",
      profilePath = "/path/to/profile.jpg",
      biography = "biography",
      deathday = "2023-10-01",
      placeOfBirth = "Los Angeles",
      popularity = 85.6f,
      name = "Your Name",
      id = 12345,
      adult = false,
      homepage = "https://example.com"
    )

    val parcel = Parcel.obtain()
    detailPerson.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val detailPerson = DetailPerson()

    val contentsValue = detailPerson.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
