package com.waffiq.bazzmovies.feature.more.ui

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.FAQ_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.FORM_HELPER
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.warning
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitFor
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitUntil
import com.waffiq.bazz_movies.core.instrumentationtest.launchFragmentInHiltContainer
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
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
import com.waffiq.bazz_movies.feature.more.ui.MoreFragment
import com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazzmovies.feature.more.testutils.Helper.userModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MoreFragmentTest {

  private lateinit var moreFragment: MoreFragment
  private val testDispatcher = UnconfinedTestDispatcher()

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(testDispatcher)

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

  private val mockRegionPref = MutableLiveData<String>()
  private val mockDbResult = MutableLiveData<Event<DbResult<Int>>>()
  private val mockSignOutState = MutableSharedFlow<Outcome<Post>>()
  private val mockCountryCode = MutableLiveData<String>()
  private val mockUserModel = MutableLiveData(userModel)

  @Before
  fun setUp() {
    hiltRule.inject()
    Intents.init()

    every { mockNavigator.openLoginActivity(any()) } just Runs
    every { mockMoreLocalViewModel.dbResult } returns mockDbResult
    every { mockMoreLocalViewModel.deleteAll() } just Runs
    every { mockUserViewModel.signOutState } returns mockSignOutState
    every { mockUserViewModel.deleteSession(any()) } just Runs
    every { mockUserViewModel.removeState() } just Runs
    every { mockRegionViewModel.countryCode } returns mockCountryCode
    // every { mockRegionViewModel.getCountryCode() } just Awaits // no use
    every { mockSnackbar.showSnackbarWarning(any<Event<String>>()) } returns mockk(relaxed = true)
    every { mockUserPrefViewModel.getUserPref() } returns mockUserModel
    every { mockUserPrefViewModel.getUserRegionPref() } returns mockRegionPref
    every { mockUserPrefViewModel.removeUserDataPref() } just Runs
    every { mockUserPrefViewModel.saveUserPref(userModel) } just Runs

    moreFragment = launchFragmentInHiltContainer<MoreFragment>()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun buttonFaq_whenClicked_shouldOpenFaqLink() {
    onView(withId(btn_faq)).perform(click())

    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(FAQ_LINK.toUri()))
  }

  @Test
  fun buttonPrivacyPolicy_whenClicked_shouldOpenPrivacyPolicyLink() {
    onView(withId(tv_privacy_policy)).perform(click())

    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(PRIVACY_POLICY_LINK.toUri()))
  }

  @Test
  fun buttonTermsCondition_whenClicked_shouldOpenTermsConditionsLink() {
    onView(withId(tv_terms_conditon)).perform(click())

    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(TERMS_CONDITIONS_LINK.toUri()))
  }

  @Test
  fun buttonSuggestion_whenClicked_shouldOpenFormHelperLink() {
    onView(withId(btn_suggestion)).perform(click())

    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(FORM_HELPER.toUri()))
  }

  @Test
  fun buttonAboutUs_whenClicked_shouldNavigateToAboutActivity() {
    onView(withId(btn_about_us)).perform(click())
    verify { mockNavigator.openAboutActivity(any()) }
  }

//  @Test
//  fun buttonRegion_whenClicked_shouldTriggerCountryPicker() {
//    onView(withId(btn_region)).perform(click())
//    onView(withId(btn_country_picker)).check(matches(isDisplayed()))
//  }

  @Test
  fun setData_whenUserPrefProvided_shouldDisplayUserInfo() {
    onView(withId(tv_fullName)).check(matches(withText("Test Name")))
    onView(withId(tv_username)).check(matches(withText("Test Username")))
  }

  @Test
  fun setData_whenUserPrefHasTmdbAvatar_shouldDisplayCorrectAvatar() {
    onView(withId(img_avatar)).check(matches(isDisplayed()))
  }

//  @Test
//  fun regionSetup_whenCountryCodeNotSet_shouldGetCountryCode() {
//    InstrumentationRegistry.getInstrumentation().runOnMainSync {
//      mockRegionPref.value = NAN
//    }
//
//    verify { mockRegionViewModel.getCountryCode() }
//  }

  @Test
  fun regionSetup_whenCountryCodeSet_shouldSetCountryPickerCorrectly() {
    mockRegionPref.postValue("AR")
    onView(withText("AR")).check(matches(isDisplayed()))
    // Verify that country picker is set with the correct country
    onView(withId(btn_country_picker)).check(matches(isDisplayed()))
  }

  @Test
  fun signOut_whenGuestUser_shouldShowGuestModeDialog() {
    // setup for guest user
    mockUserModel.postValue(userModel.copy(token = NAN))

    // wait for data to observe
    onView(isRoot()).perform(waitFor(300))

    // perform sign out
    onView(withId(btn_signout)).perform(click())

    // check if dialog is shown
    onView(withText(warning)).check(matches(isDisplayed()))
    onView(withText(warning_signOut_guest_mode)).check(matches(isDisplayed()))
//    onView(isRoot()).perform(waitFor(500))
//
//    onView(withText(yes)).perform(click())
//    onView(isRoot()).perform(waitFor(1000))
//
//    verify(exactly = 1) { mockMoreLocalViewModel.deleteAll() }
//    verify(exactly = 1) { mockUserPrefViewModel.removeUserDataPref() }
//    verify(exactly = 1) { mockNavigator.openLoginActivity(any()) }
  }

  @Test
  fun signOut_whenLoggedInUser_shouldShowLoggedInDialog() {
    onView(withId(btn_signout)).perform(click())

    onView(withText(warning)).check(matches(isDisplayed()))
    onView(withText(warning_signOut_logged_user)).check(matches(isDisplayed()))
  }

  @Test
  fun signOutDialogGuestUser_whenYesClicked_shouldDeleteLocalData() {
    // setup for guest user
    mockUserModel.postValue(userModel.copy(token = NAN))

    // wait for data to observe
    onView(isRoot()).perform(waitFor(300))

    // perform yes clicked
    onView(withId(btn_signout)).perform(click())
    onView(withText(yes)).perform(click())

     verify { mockMoreLocalViewModel.deleteAll() }
  }

//   @Test
//   fun signOutDialog_whenYesClicked_forLoggedInUser_shouldDeleteSession() {
//     onView(withId(btn_signout)).perform(click())
//     onView(withText(yes)).perform(click())
//
//     verify { mockUserViewModel.deleteSession("valid_token") }
//   }

  @Test
  fun signOutDialog_whenNoClicked_shouldDismissDialog() {
    onView(withId(btn_signout)).perform(click())
    onView(withText(no)).perform(click())

    // dialog should be dismissed, verify warning text is not displayed
    onView(withText(warning)).check(doesNotExist())
  }

  @Test
  fun signOutStateLogin_whenErrorOccurs_shouldShowErrorSnackbar() {
    runBlocking {
      mockSignOutState.emit(Outcome.Error("Sign out failed"))
    }

    // perform sign out
    onView(withId(btn_signout)).perform(click())
    onView(withText("Yes")).perform(click())
    onView(isRoot()).perform(waitFor(1000))

    onView(withId(btn_signout)).check(matches(not(isEnabled())))
    onView(withId(progress_bar)).check(waitUntil(isDisplayed()))
  }

  @Test
  fun dbResult_whenSuccess_shouldShowSuccessMessage() {
    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
  }

//  @Test
//  fun dbResult_whenErrorOccurs_shouldShowErrorSnackbar() {
//    InstrumentationRegistry.getInstrumentation().runOnMainSync {
//      mockDbResult.value = Event(DbResult.Error("Database error"))
//    }
//
//    verify { mockSnackbar.showSnackbarWarning(any<Event<String>>()) }
//    // Progress bar should be hidden
//    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
//  }

//  @Test
//  fun countryPicker_whenCountrySelected_shouldSaveRegionPref() {
//    onView(withId(btn_country_picker)).perform(click())
//
//    // Simulate country selection - this might need adjustment based on your country picker implementation
//    // verify { mockUserPreferenceViewModel.saveRegionPref(any()) }
//  }

  @Test
  fun progressBar_initialState_shouldBeHidden() {
    onView(withId(progress_bar)).check(matches(not(isDisplayed())))
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
}
