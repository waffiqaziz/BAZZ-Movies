package com.waffiq.bazz_movies.core.instrumentationtest

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomAssertions {

  fun waitFor(millis: Long = 1000): ViewAction =
    object : ViewAction {
      override fun getConstraints(): Matcher<View> = isRoot()
      override fun getDescription(): String = "Wait for $millis milliseconds"
      override fun perform(uiController: UiController, view: View) =
        uiController.loopMainThreadForAtLeast(millis)
    }

  fun isPasswordHidden(): Matcher<View> =
    object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("Password is hidden")
      }

      // check if password is hidden
      override fun matchesSafely(editText: EditText): Boolean =
        editText.transformationMethod is PasswordTransformationMethod
    }

  fun withErrorText(expectedError: String): Matcher<View> =
    object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("with error text: $expectedError")
      }

      override fun matchesSafely(editText: EditText): Boolean =
        editText.error?.toString() == expectedError
    }

  fun withoutError(): Matcher<View> =
    object : BoundedMatcher<View, EditText>(EditText::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("without error text")
      }

      override fun matchesSafely(editText: EditText): Boolean = editText.error == null
    }
}
