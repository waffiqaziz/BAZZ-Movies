package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.content.Context
import android.os.Looper.getMainLooper
import android.widget.ImageButton
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth_selected
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnAction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

/**
 * This test did not test the animation transitions themselves (like scale or alpha)
 * that’s more suitable for UI or screenshot testing
 */
@RunWith(RobolectricTestRunner::class)
class ButtonImageChangerTest {

  private lateinit var context: Context
  private lateinit var imageButton: ImageButton

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    imageButton = ImageButton(context)
  }

  @Test
  fun changeBtnAction_withDifferentTag_setsNewTagAndDrawable() {
    imageButton.tag = null

    changeBtnAction(
      button = imageButton,
      isActivated = false,
      iconActive = ic_hearth_selected,
      iconInactive = ic_hearth,
    )
    shadowOf(getMainLooper()).idle() // to trigger onAnimationEnd
    checkImageButtonTagAndDrawable(ic_hearth)
  }

  @Test
  fun changeBtnAction_isActive_setsNewDrawable() {
    imageButton.tag = null

    changeBtnAction(
      button = imageButton,
      isActivated = true,
      iconActive = ic_hearth_selected,
      iconInactive = ic_hearth,
    )
    shadowOf(getMainLooper()).idle()
    checkImageButtonTagAndDrawable(ic_hearth_selected)
  }

  @Test
  fun changeBtnAction_withSameTag_doesNothing() {
    imageButton.tag = ic_hearth_selected

    changeBtnAction(
      button = imageButton,
      isActivated = true,
      iconActive = ic_hearth_selected,
      iconInactive = ic_hearth,
    )
    assertEquals(ic_hearth_selected, imageButton.tag)
  }

  @Test
  fun changeBtnAction_withDifferentExistingTag_updatesDrawable() {
    imageButton.tag = ic_hearth

    changeBtnAction(
      button = imageButton,
      isActivated = true,
      iconActive = ic_hearth_selected,
      iconInactive = ic_hearth,
    )
    shadowOf(getMainLooper()).idle()
    checkImageButtonTagAndDrawable(ic_hearth_selected)
  }

  private fun checkImageButtonTagAndDrawable(icon: Int) {
    assertEquals(icon, imageButton.tag)
    val shadowDrawable = shadowOf(imageButton.drawable)
    assertEquals(icon, shadowDrawable.createdFromResId)
  }
}
