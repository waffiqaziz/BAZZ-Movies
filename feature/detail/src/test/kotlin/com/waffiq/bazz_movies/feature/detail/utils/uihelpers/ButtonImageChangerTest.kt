package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.content.Context
import android.widget.ImageButton
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark_selected
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth_selected
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnFavoriteBG
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnWatchlistBG
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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
  fun changeBtnWatchlistBG_withDifferentTag_setsNewTagAndDrawable() {
    imageButton.tag = null

    changeBtnWatchlistBG(imageButton, isActivated = true)
    assertEquals(ic_bookmark_selected, imageButton.tag)
  }

  @Test
  fun changeBtnWatchlistBG_isNotActive_setsNewDrawable() {
    imageButton.tag = null

    changeBtnWatchlistBG(imageButton, isActivated = false)
    assertEquals(ic_bookmark, imageButton.tag)
  }

  @Test
  fun changeBtnWatchlistBG_withSameTag_doesNothing() {
    imageButton.tag = ic_bookmark_selected

    changeBtnWatchlistBG(imageButton, isActivated = true)
    assertEquals(ic_bookmark_selected, imageButton.tag)
  }

  @Test
  fun changeBtnFavoriteBG_withDifferentTag_setsNewTagAndDrawable() {
    imageButton.tag = null

    changeBtnFavoriteBG(imageButton, isActivated = false)
    assertEquals(ic_hearth, imageButton.tag)
  }

  @Test
  fun changeBtnFavoriteBG_isActive_setsNewDrawable() {
    imageButton.tag = ic_hearth_selected

    changeBtnFavoriteBG(imageButton, isActivated = true)
    assertEquals(ic_hearth_selected, imageButton.tag)
  }

  @Test
  fun changeBtnFavoriteBG_withSameTag_doesNothing() {
    imageButton.tag = ic_hearth

    changeBtnFavoriteBG(imageButton, isActivated = false)
    assertEquals(ic_hearth, imageButton.tag)
  }
}
