package com.waffiq.bazz_movies.feature.person.utils.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
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
  @Config(sdk = [Build.VERSION_CODES.O])
  fun `test getAgeDeath with LocalDate for API 26 and up`() {
    val birthDate = "1990-05-15"
    val deathDate = "2020-10-10"

    val age = getAgeDeath(birthDate, deathDate)
    assertEquals(30, age)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.M])
  fun `test getAgeDeath with Calendar for API lower than 26`() {
    val birthDate = "1990-05-15"
    val deathDate = "2024-10-10"

    val age = getAgeDeath(birthDate, deathDate)
    assertEquals(34, age)
  }

  @Test
  fun `test setupSocialLink when social ID is not null`() {
    val activity = Robolectric.buildActivity(Activity::class.java).create().get()
    val imageView = ImageView(activity)
    val socialId = "12345"
    val baseUrl = "https://social.com/"

    activity.setupSocialLink(socialId, imageView, baseUrl)
    assertTrue(imageView.isVisible)

    imageView.performClick()

    // use Robolectric to capture the started activity
    val shadowActivity = shadowOf(activity)
    val nextIntent = shadowActivity.nextStartedActivity

    // assert that the intent is as expected after image clicked
    assertNotNull(nextIntent)
    assertEquals(Intent.ACTION_VIEW, nextIntent?.action)
    assertEquals(Uri.parse("$baseUrl$socialId"), nextIntent?.data)
  }

  @Test
  fun `test setupSocialLink when social ID is null`() {
    val context: Context = mockk(relaxed = true)
    val imageView: ImageView = mockk(relaxed = true)
    val socialId: String? = null
    val baseUrl = "https://social.com/"

    context.setupSocialLink(socialId, imageView, baseUrl)
    verify { imageView.visibility = View.GONE } // Verify visibility is set to GONE
    verify(exactly = 0) { imageView.setOnClickListener(any()) } // Verify no click listener is set
  }
}
