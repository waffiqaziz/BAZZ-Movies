package com.waffiq.bazz_movies.core.domain

import android.os.Parcel
import android.os.Parcelable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserModelTest {

  private val userModelValid = UserModel(
    userId = 1,
    name = "John Doe",
    username = "johndoe",
    password = "securePass123",
    region = "US",
    token = "abcd1234token",
    isLogin = true,
    gravatarHast = "123abcHash",
    tmdbAvatar = "/avatar.jpg"
  )

  private val userModelGravatarHashNull = userModelValid.copy(gravatarHast = null)

  @Test
  fun userModel_withValidValue_returnsCorrectData() {
    assertEquals(1, userModelValid.userId)
    assertEquals("John Doe", userModelValid.name)
    assertEquals("johndoe", userModelValid.username)
    assertEquals("securePass123", userModelValid.password)
    assertEquals("US", userModelValid.region)
    assertEquals("abcd1234token", userModelValid.token)
    assertTrue(userModelValid.isLogin)
    assertEquals("123abcHash", userModelValid.gravatarHast)
    assertEquals("/avatar.jpg", userModelValid.tmdbAvatar)
  }

  @Test
  fun parcelable_whenAllFieldsAreValid_readsAndWritesCorrectly() {
    // obtain the CREATOR field
    @Suppress("UNCHECKED_CAST")
    val creator =
      UserModel::class.java.getField("CREATOR").get(null) as Parcelable.Creator<UserModel>
    val parcel = Parcel.obtain()
    userModelValid.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)

    val fromParcel = creator.createFromParcel(parcel)
    assertEquals(userModelValid, fromParcel)
    parcel.recycle()
  }

  @Test
  fun parcelable_whenAllFieldsAreNull_readsAndWritesCorrectly() {
    @Suppress("UNCHECKED_CAST")
    val creator =
      UserModel::class.java.getField("CREATOR").get(null) as Parcelable.Creator<UserModel>
    val parcel = Parcel.obtain()
    userModelGravatarHashNull.writeToParcel(parcel, 0)

    parcel.setDataPosition(0)
    val fromParcel = creator.createFromParcel(parcel)

    assertEquals(userModelGravatarHashNull, fromParcel)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithValueIsValid_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    userModelValid.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun writeToParcel_whenCalledWithNullValue_performsWriteWithoutCrash() {
    val parcel = Parcel.obtain()
    userModelGravatarHashNull.writeToParcel(parcel, 0)
    parcel.recycle()
  }

  @Test
  fun describeContents_whenCalled_returnsZero() {
    val contentsValue = userModelValid.describeContents()

    // assert the typical return value for describeContents
    assertEquals(0, contentsValue)
  }
}
