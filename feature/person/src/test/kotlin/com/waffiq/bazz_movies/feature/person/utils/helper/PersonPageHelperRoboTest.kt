package com.waffiq.bazz_movies.feature.person.utils.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.getAgeDeath
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.setupSocialLink
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class PersonPageHelperRoboTest {

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getAgeDeath_whenDeathBeforeBirthday_returnsReducedAge() {
    val age = getAgeDeath(dateBirth = "2000-05-10", dateDeath = "2021-03-15")
    assertEquals(20, age) // should be 20 instead of 21
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getAgeDeath_whenApiLevelIs26OrAbove_returnsCorrectAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2020-10-10")
    assertEquals(30, age)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.M])
  fun getAgeDeath_whenApiLevelIsBelow26_returnsCorrectAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2024-10-10")
    assertEquals(34, age)
  }

  @Test
  fun getAgeDeath_whenDatesAreValid_returnsCalculatedAge() {
    val age = getAgeDeath(dateBirth = "1990-05-15", dateDeath = "2024-10-10")
    assertEquals(34, age)
  }

  @Test
  fun setupSocialLink_whenSocialIdIsNotNull_showsIconButtonAndStartsIntent() {
    val activity = Robolectric.buildActivity(Activity::class.java).create().get()
    val iconButton = Button(activity)
    val socialId = "12345"
    val baseUrl = "https://social.com/"

    activity.setupSocialLink(socialId, iconButton, baseUrl)
    assertTrue(iconButton.isVisible)

    iconButton.performClick()

    // use Robolectric to capture the started activity
    val shadowActivity = shadowOf(activity)
    val nextIntent = shadowActivity.nextStartedActivity

    // expected intent after image clicked
    assertNotNull(nextIntent)
    assertEquals(Intent.ACTION_VIEW, nextIntent?.action)
    assertEquals(Uri.parse("$baseUrl$socialId"), nextIntent?.data)
  }

  @Test
  fun setupSocialLink_whenSocialIdIsNull_hidesIconButtonAndSkipsClickListener() {
    val context: Context = mockk(relaxed = true)
    val iconButton: Button = mockk(relaxed = true)

    context.setupSocialLink(
      socialId = null,
      iconButton = iconButton,
      baseUrl = "https://social.com/"
    )
    verify { iconButton.visibility = View.GONE } // verify visibility is set to GONE
    verify(exactly = 0) { iconButton.setOnClickListener(any()) } // verify no click listener is set
  }

  @Test
  fun setupSocialLink_whenSocialIdIsEmpty_hidesImageViewAndSkipsClickListener() {
    val context: Context = mockk(relaxed = true)
    val iconButton: Button = mockk(relaxed = true)

    context.setupSocialLink(socialId = "", iconButton = iconButton, baseUrl = "https://social.com/")
    verify { iconButton.visibility = View.GONE }
    verify(exactly = 0) { iconButton.setOnClickListener(any()) }
  }
}
