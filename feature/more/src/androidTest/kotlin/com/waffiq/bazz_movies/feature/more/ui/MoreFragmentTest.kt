package com.waffiq.bazz_movies.feature.more.ui

import android.content.Intent
import android.view.View
import android.widget.EditText
import androidx.core.net.toUri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.common.utils.Constants.FAQ_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.FORM_HELPER
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.PRIVACY_POLICY_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TERMS_CONDITIONS_LINK
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.feature.more.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.more.R.id.btn_country_picker
import com.waffiq.bazz_movies.feature.more.R.id.btn_faq
import com.waffiq.bazz_movies.feature.more.R.id.btn_privacy_policy
import com.waffiq.bazz_movies.feature.more.R.id.btn_region
import com.waffiq.bazz_movies.feature.more.R.id.btn_signout
import com.waffiq.bazz_movies.feature.more.R.id.btn_suggestion
import com.waffiq.bazz_movies.feature.more.R.id.btn_terms_condition
import com.waffiq.bazz_movies.feature.more.R.id.img_avatar
import com.waffiq.bazz_movies.feature.more.R.id.progress_bar
import com.waffiq.bazz_movies.feature.more.R.id.tv_fullName
import com.waffiq.bazz_movies.feature.more.R.id.tv_username
import com.waffiq.bazz_movies.feature.more.testutils.DefaultMoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.testutils.Helper.userModel
import com.waffiq.bazz_movies.feature.more.testutils.MoreFragmentTestHelper
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreLocalViewModel
import com.waffiq.bazz_movies.feature.more.ui.viewmodel.MoreUserViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matcher
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
    btn_faq.performClick()
    checkIntentData(FAQ_LINK)
  }

  @Test
  fun buttonPrivacyPolicy_whenClicked_shouldOpenPrivacyPolicyLink() {
    btn_privacy_policy.performClick()
    checkIntentData(PRIVACY_POLICY_LINK)
  }

  @Test
  fun buttonTermsCondition_whenClicked_shouldOpenTermsConditionsLink() {
    btn_terms_condition.performClick()
    checkIntentData(TERMS_CONDITIONS_LINK)
  }

  @Test
  fun buttonSuggestion_whenClicked_shouldOpenFormHelperLink() {
    btn_suggestion.performClick()
    checkIntentData(FORM_HELPER)
  }

  @Test
  fun buttonAboutUs_whenClicked_shouldNavigateToAboutActivity() {
    btn_about_us.performClick()
    verify { mockNavigator.openAboutActivity(any()) }
  }

  @Test
  fun setData_whenUserPrefProvided_shouldDisplayUserInfo() {
    tv_fullName.doesHaveText("Test Name")
    tv_username.doesHaveText("Test Username")
  }

  @Test
  fun setData_whenUserPrefHasTmdbAvatar_shouldDisplayCorrectAvatar() {
    img_avatar.isDisplayed()
  }

  @Test
  fun buttonRegion_whenClicked_shouldOpenDialog() {
    btn_region.performClick()
    "Afghanistan".isDisplayed()
  }

  @Test
  fun regionSetup_whenCountryCodeSet_shouldSetCountryPickerCorrectly() {
    mockRegionPref.postValue("AR")
    "AR".isDisplayed()
    // verify country picker is set with the correct country
    btn_country_picker.isDisplayed()
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
      gravatarHash = "testHash123",
      tmdbAvatar = null,
    )
    checkAvatarIsVisible(userWithGravatar, isDisplayed())
  }

  @Test
  fun setData_whenUserHasTmdbAvatarButNoGravatar_shouldDisplayAvatar() {
    val userWithTmdb = userModel.copy(
      gravatarHash = null,
      tmdbAvatar = "tmdbAvatar123",
    )
    checkAvatarIsVisible(userWithTmdb, isDisplayed())
  }

  @Test
  fun setData_whenUserHasNoAvatars_shouldUseDefaultAvatar() {
    val userWithoutAvatars = userModel.copy(
      gravatarHash = null,
      tmdbAvatar = null,
    )
    checkAvatarIsVisible(userWithoutAvatars, isDisplayed())
  }

  @Test
  fun setData_whenUserHasEmptyAvatars_shouldUseDefaultAvatar() {
    val userWithEmptyAvatars = userModel.copy(
      gravatarHash = "",
      tmdbAvatar = "",
    )
    checkAvatarIsVisible(userWithEmptyAvatars, isDisplayed())
  }

  @Test
  fun progressBar_initialState_shouldBeHidden() {
    progress_bar.isNotDisplayed()
  }

  @Test
  fun btnCountryPicker_selectCountry_callsCorrectFunction() {
    btn_country_picker.performClick()
    onView(isAssignableFrom(EditText::class.java))
      .inRoot(isDialog())
      .perform(typeText("Ind"), closeSoftKeyboard())
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()

    onView(withText("Indonesia"))
      .inRoot(isDialog())
      .perform(click())
    shortDelay()

    verify { mockUserPrefViewModel.saveRegionPref(any()) }
  }

  @Test
  fun allButtons_shouldBeDisplayed() {
    btn_faq.isDisplayed()
    btn_privacy_policy.isDisplayed()
    btn_terms_condition.isDisplayed()
    btn_suggestion.isDisplayed()
    btn_about_us.isDisplayed()
    btn_signout.isDisplayed()
    btn_region.isDisplayed()
    btn_country_picker.isDisplayed()
  }

  @Test
  fun userInfo_shouldBeDisplayed() {
    tv_fullName.isDisplayed()
    tv_username.isDisplayed()
    img_avatar.isDisplayed()
  }

  private fun checkIntentData(link: String) {
    intended(hasAction(Intent.ACTION_VIEW))
    intended(hasData(link.toUri()))
  }

  private fun checkAvatarIsVisible(userModel: UserModel, viewMatcher: Matcher<View>) {
    mockUserModel.postValue(userModel)
    shortDelay()

    onView(withId(img_avatar)).check(matches(viewMatcher))
    tv_fullName.doesHaveText(userModel.name)
  }
}
