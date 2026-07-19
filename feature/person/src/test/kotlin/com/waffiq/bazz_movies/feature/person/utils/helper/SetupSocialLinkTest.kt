package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.setupSocialLink
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SetupSocialLinkTest {

  private val uriLauncher: UriLauncher = mockk()
  private val iconButton: Button = mockk(relaxed = true)

  private val baseUrl = "https://social.com/"

  @Before
  fun setup() {
    every { uriLauncher.launch(any()) } just Runs
  }

  @Test
  fun setupSocialLink_whenSocialIdExists_launchesUrl() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val button = Button(context)

    val socialId = "12345"

    setupSocialLink(
      socialId,
      button,
      baseUrl,
      uriLauncher,
    )

    assertTrue(button.isVisible)

    button.performClick()

    verify(exactly = 1) {
      uriLauncher.launch("$baseUrl$socialId")
    }
  }

  @Test
  fun setupSocialLink_whenSocialIdIsNull_hidesIconButtonAndSkipsClickListener() {
    setupSocialLink(
      socialId = null,
      iconButton = iconButton,
      baseUrl = baseUrl,
      uriLauncher = uriLauncher,
    )
    verify { iconButton.visibility = View.GONE }
    verify(exactly = 0) { iconButton.setOnClickListener(any()) }
  }

  @Test
  fun setupSocialLink_whenSocialIdIsEmpty_hidesImageViewAndSkipsClickListener() {
    setupSocialLink("", iconButton, baseUrl, uriLauncher)
    verify { iconButton.visibility = View.GONE }
    verify(exactly = 0) { iconButton.setOnClickListener(any()) }
  }
}
