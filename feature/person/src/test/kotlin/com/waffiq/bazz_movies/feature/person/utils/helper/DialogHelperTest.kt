package com.waffiq.bazz_movies.feature.person.utils.helper

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity.Companion.DIALOG_ALPHA
import com.waffiq.bazz_movies.feature.person.utils.helper.DialogHelper.setupTransparentDialog
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DialogHelperTest {

  @Test
  fun setupTransparentDialog_whenCalled_configuresWindowCorrectly() {
    val dialog = mockk<Dialog>(relaxed = true)
    val window = mockk<Window>(relaxed = true)

    every { dialog.window } returns window

    dialog.setupTransparentDialog()

    verify { window.setDimAmount(DIALOG_ALPHA) }

    verify {
      window.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
      )
    }

    verify {
      window.setBackgroundDrawable(ofType<ColorDrawable>())
    }
  }

  @Test
  fun setupTransparentDialog_withNullWindow_doNothing() {
    val dialog = mockk<Dialog>(relaxed = true)

    every { dialog.window } returns null
    assertFailsWith<IllegalArgumentException> {
      dialog.setupTransparentDialog()
    }
  }
}
