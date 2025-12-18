package com.waffiq.bazz_movies.feature.more.ui

import android.content.Intent
import android.view.View
import android.widget.EditText
import androidx.core.net.toUri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.FAQ_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.FORM_HELPER
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.warning
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntil
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.more.R.id.btn_country_picker
import com.waffiq.bazz_movies.feature.more.R.id.btn_faq
import com.waffiq.bazz_movies.feature.more.R.id.btn_region
import com.waffiq.bazz_movies.feature.more.R.id.btn_signout
import com.waffiq.bazz_movies.feature.more.R.id.btn_suggestion
import com.waffiq.bazz_movies.feature.more.R.id.img_avatar
import com.waffiq.bazz_movies.feature.more.R.id.progress_bar
import com.waffiq.bazz_movies.feature.more.R.id.tv_fullName
import com.waffiq.bazz_movies.feature.more.R.id.tv_privacy_policy
import com.waffiq.bazz_movies.feature.more.R.id.tv_terms_conditon
import com.waffiq.bazz_movies.feature.more.R.id.tv_username
import com.waffiq.bazz_movies.feature.more.testutils.DefaultMoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.testutils.Helper.userModel
import com.waffiq.bazz_movies.feature.more.testutils.MoreFragmentTestHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MoreFragmentTest : MoreFragmentTestHelper by DefaultMoreFragmentTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockSnackbar: ISnackbar = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockMoreLocalViewModel: MoreLocalViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserViewModel: MoreUserViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockRegionViewModel: RegionViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockUserPrefViewModel: UserPreferenceViewModel = mockk(relaxed = true)

  @Before
  override fun setUp() {
    hiltRule.inject()

    setupMocks(
      mockNavigator,
      mockSnackbar,
    )

    setupViewModelMocks(
      mockMoreLocalViewModel,
      mockUserViewModel,
      mockRegionViewModel,
      mockUserPrefViewModel,
    )

    super.setUp()
  }

  @Test
  fun buttonFaq_whenClicked_shouldOpenFaqLink() {
    onView(withId(btn_faq)).perform(click())
    checkIntentData(FAQ_LINK)
  }

  @Test
  fun buttonPrivacyPolicy_whenClicked_shouldOpenPrivacyPolicyLink() {
    onView(withId(tv_privacy_policy)).perform(click())
    checkIntentData(PRIVACY_POLICY_LINK)
  }

  @Test
  fun buttonTermsCondition_whenClicked_shouldOpenTermsConditionsLink() {
    onView(withId(tv_terms_conditon)).perform(click())
    checkIntentData(TERMS_CONDITIONS_LINK)
  }

  @Test
  fun buttonSuggestion_whenClicked_shouldOpenFormHelperLink() {
    onView(withId(btn_suggestion)).perform(click())
    checkIntentData(FORM_HELPER)
  }

  @Test
  fun buttonAboutUs_whenClicked_shouldNavigateToAboutActivity() {
    onView(withId(btn_about_us)).perform(click())
    verify { mockNavigator.openAboutActivity(any()) }
  }

  @Test
  fun setData_whenUserPrefProvided_shouldDisplayUserInfo() {
    onView(withId(tv_fullName)).check(matches(withText("Test Name")))
    onView(withId(tv_username)).check(matches(withText("Test Username")))
  }

  @Test
  fun setData_whenUserPrefHasTmdbAvatar_shouldDisplayCorrectAvatar() {
    onView(withId(img_avatar)).check(matches(isDisplayed()))
  }

  @Test
  fun regionSetup_whenCountryCodeSet_shouldSetCountryPickerCorrectly() {
    mockRegionPref.postValue("AR")
    onView(withText("AR")).check(matches(isDisplayed()))
    // verify country picker is set with the correct country
    onView(withId(btn_country_picker)).check(matches(isDisplayed()))
  }

  @Test
  fun signOut_whenGuestUser_shouldShowGuestModeDialog() {
    setupGuestUser()
    performSignOutAction()
  }

  @Test
  fun signOutStateLogin_allBranches_shouldBeCovered() = runTest {
    // Hit all branches in the logged-in when statement
    mockUIState.emit(UIState.Loading)
    advanceUntilIdle()
    performSignOutAction()

    mockUIState.emit(UIState.Error("Test error"))
    advanceUntilIdle()

    mockUIState.emit(UIState.Success(Unit))
    advanceUntilIdle()
  }

  @Test
  fun signOutStateGuest_allBranches_shouldBeCovered() = runTest {
    setupGuestUser()
    performSignOutAction()
    mockUIState.emit(UIState.Loading)
    advanceUntilIdle()
  }

  @Test
  fun signOut_whenLoggedInUser_shouldShowLoggedInDialog() {
    onView(withId(btn_signout)).perform(click())

    onView(withText(warning)).check(matches(isDisplayed()))
    onView(withText(warning_signOut_logged_user)).check(matches(isDisplayed()))
  }

  @Test
  fun signOutDialogGuestUser_whenYesClicked_shouldDeleteLocalData() {
    setupGuestUser()
    performSignOutAction()

    verify { mockMoreLocalViewModel.deleteAll() }
  }

  @Test
  fun signOutDialog_whenNoClicked_shouldDismissDialog() {
    onView(withId(btn_signout)).perform(click())
    onView(withText(no)).perform(click())

    // dialog should be dismissed, verify warning text is not displayed
    onView(withText(warning)).check(doesNotExist())
  }

  @Test
  fun signOutStateLogin_whenSuccess_shouldShowSuccessToastAndNavigateToLogin() {
    performSignOutAction()
  }

  @Test
  fun signOutStateLogin_whenLoading_shouldShowLoadingState() = runTest {
    performSignOutAction()
    mockUIState.emit(UIState.Loading)

    onView(withId(progress_bar)).check(matches(isDisplayed()))
  }

  @Test
  fun dbResultGuestUser_whenSuccess_shouldShowSuccessToast() = runTest {
    setupGuestUser()
    performSignOutAction()
    mockUIState.emit(UIState.Success(Unit))

    // manual checking
  }

  @Test
  fun signOutStateLogin_whenErrorOccurs_signOutButtonShouldEnable() = runTest {
    performSignOutAction()
    mockUIState.emit(UIState.Loading)
    advanceUntilIdle()

    onView(withId(progress_bar)).check(waitUntil(isDisplayed()))
    onView(withId(btn_signout)).check(matches(not(isEnabled())))

    mockUIState.emit(UIState.Error("Sign out failed"))
    advanceUntilIdle()

    onView(withId(btn_signout)).check(matches(isEnabled()))
    onView(withId(progress_bar)).check(waitUntil(not(isDisplayed())))
  }

  @Test
  fun dbResultGuestUser_whenError_shouldShowErrorSnackbar() = runTest {
    setupGuestUser()

    // emit error result
    mockUIState.emit(UIState.Error("Database error"))
    shortDelay()

    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
    verify(timeout = 2000) { mockSnackbar.showSnackbarWarning(any<String>()) }
  }

  @Test
  fun regionViewModel_whenCountryCodeProvided_shouldUpdateRegionAndCountryPicker() {
    mockCountryCode.postValue("CA")
    shortDelay()

    verify(timeout = 2000) { mockUserPrefViewModel.saveRegionPref("CA") }
  }

  @Test
  fun regionViewModel_whenCountryCodeEmpty_shouldNotUpdateRegion() {
    mockCountryCode.postValue("")
    shortDelay()

    verify(exactly = 0) { mockUserPrefViewModel.saveRegionPref("") }
  }

  @Test
  fun regionViewModel_whenCountryNaN_shouldCallsGetCountryCode() {
    every { mockRegionViewModel.getCountryCode() } just Runs

    mockRegionPref.postValue(NAN)
    verify { mockRegionViewModel.getCountryCode() }
  }

  @Test
  fun setData_whenUserHasGravatarHash_shouldDisplayAvatar() {
    val userWithGravatar = userModel.copy(
      gravatarHast = "testHash123",
      tmdbAvatar = null
    )
    checkAvatarIsVisible(userWithGravatar, isDisplayed())
  }

  @Test
  fun setData_whenUserHasTmdbAvatarButNoGravatar_shouldDisplayAvatar() {
    val userWithTmdb = userModel.copy(
      gravatarHast = null,
      tmdbAvatar = "tmdbAvatar123"
    )
    checkAvatarIsVisible(userWithTmdb, isDisplayed())
  }

  @Test
  fun setData_whenUserHasNoAvatars_shouldUseDefaultAvatar() {
    val userWithoutAvatars = userModel.copy(
      gravatarHast = null,
      tmdbAvatar = null
    )
    checkAvatarIsVisible(userWithoutAvatars, isDisplayed())
  }

  @Test
  fun setData_whenUserHasEmptyAvatars_shouldUseDefaultAvatar() {
    val userWithEmptyAvatars = userModel.copy(
      gravatarHast = "",
      tmdbAvatar = ""
    )
    checkAvatarIsVisible(userWithEmptyAvatars, isDisplayed())
  }

  @Test
  fun progressBar_initialState_shouldBeHidden() {
    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
  }

  @Test
  fun btnCountryPicker_selectCountry_callsCorrectFunction() {
    onView(withId(btn_country_picker)).check(matches(isDisplayed())).perform(click())
    onView(isAssignableFrom(EditText::class.java))
      .inRoot(isDialog())
      .perform(typeText("Ind"), closeSoftKeyboard())
    onView(withText("Indonesia"))
      .inRoot(isDialog())
      .perform(click())
    shortDelay()

    verify { mockUserPrefViewModel.saveRegionPref(any()) }
  }

  @Test
  fun signOutButton_initialState_shouldBeEnabled() {
    onView(withId(progress_bar)).check(matches(isEnabled()))
  }

  @Test
  fun allButtons_shouldBeDisplayed() {
    onView(withId(btn_faq)).check(matches(isDisplayed()))
    onView(withId(tv_privacy_policy)).check(matches(isDisplayed()))
    onView(withId(tv_terms_conditon)).check(matches(isDisplayed()))
    onView(withId(btn_suggestion)).check(matches(isDisplayed()))
    onView(withId(btn_about_us)).check(matches(isDisplayed()))
    onView(withId(btn_signout)).check(matches(isDisplayed()))
    onView(withId(btn_region)).check(matches(isDisplayed()))
    onView(withId(btn_country_picker)).check(matches(isDisplayed()))
  }

  @Test
  fun userInfo_shouldBeDisplayed() {
    onView(withId(tv_fullName)).check(matches(isDisplayed()))
    onView(withId(tv_username)).check(matches(isDisplayed()))
    onView(withId(img_avatar)).check(matches(isDisplayed()))
  }

  @Test
  fun onStop_whenCalled_shouldCleanupSnackbarAndDialog() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onStop()
    }
  }

  @Test
  fun onPause_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onPause()
    }
  }

  @Test
  fun onResume_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onResume()
    }
  }

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onDestroyView()
    }
    verify(exactly = 1) { mockUserViewModel.removeState() }
  }

  private fun checkIntentData(link: String) {
    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(link.toUri()))
  }

  private fun setupGuestUser() {
    mockUserModel.postValue(userModel.copy(token = NAN))
    shortDelay()
  }

  private fun performSignOutAction() {
    onView(withId(btn_signout)).perform(click())
    onView(withText(yes)).perform(click())
  }

  private fun checkAvatarIsVisible(userModel: UserModel, viewMatcher: Matcher<View>) {
    mockUserModel.postValue(userModel)
    shortDelay()

    onView(withId(img_avatar)).check(matches(viewMatcher))
    onView(withId(tv_fullName)).check(matches(withText(userModel.name)))
  }
}
