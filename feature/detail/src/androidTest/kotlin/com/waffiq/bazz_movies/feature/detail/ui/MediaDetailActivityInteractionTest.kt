package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.cancel
import com.waffiq.bazz_movies.core.designsystem.R.string.submit
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.btn_favorite
import com.waffiq.bazz_movies.feature.detail.R.id.btn_watchlist
import com.waffiq.bazz_movies.feature.detail.R.id.iv_poster
import com.waffiq.bazz_movies.feature.detail.R.id.rating_bar_action
import com.waffiq.bazz_movies.feature.detail.R.id.rv_genre
import com.waffiq.bazz_movies.feature.detail.R.id.score_scrollview
import com.waffiq.bazz_movies.feature.detail.R.id.tv_score_your_score
import com.waffiq.bazz_movies.feature.detail.R.id.your_score_viewGroup
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaState
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.testMediaStateRated
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestHelper
import com.waffiq.bazz_movies.feature.detail.testutils.MediaDetailActivityTestSetup
import com.waffiq.bazz_movies.feature.detail.testutils.SetRatingAction
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.DetailUserPrefViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_GENRE
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Instrumented test for [MediaDetailActivity] interactions.
 * This test checks various user interactions and UI responses in the media detail screen.
 */
@HiltAndroidTest
class MediaDetailActivityInteractionTest :
  MediaDetailActivityTestSetup by MediaDetailActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockMediaDetailViewModel: MediaDetailViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockPrefViewModel: DetailUserPrefViewModel = mockk(relaxed = true)

  @Before
  fun setup() {
    hiltRule.inject()
    setupMocks()
    Intents.init()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupMocks() {
    setupBaseMocks()
    setupObservables(mockMediaDetailViewModel)
    setupPreferencesViewModelMocks(mockPrefViewModel)
    setupMediaDetailViewModelMocks(mockMediaDetailViewModel)
    setupNavigatorMocks(mockNavigator)
  }

  @Test
  fun errorState_whenErrorOccur_showsTheToast() = runTest {
    context.launchMediaDetailActivity {
      launch {
        errorEvent.emit("Error")
      }
      advanceUntilIdle()
      // Hard to test toast in code, so must check it manually
      // https://github.com/android/android-test/issues/803
    }
  }

  @Test
  fun button_whenClicked_showsToast() {
    context.launchMediaDetailActivity {
      onView(withId(btn_favorite)).check(matches(isDisplayed())).perform(click())
      onView(withId(btn_watchlist)).check(matches(isDisplayed())).perform(click())
    }
  }

  @Test
  fun mediaStateResult_whenNull_doNothing() {
    context.launchMediaDetailActivity {
      updateState { copy(mediaStateResult = null) }
    }
  }

  @Test
  fun buttonBack_whenClicked_finishTheActivity() {
    context.launchMediaDetailActivity { scenario ->
      onView(withId(btn_back)).check(matches(isDisplayed())).perform(click())

      // check if the activity is finished
      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun swipeRefreshMovie_whenScroll_runsCorrectly() {
    context.launchMediaDetailActivity {
      // set token and region to simulate user logged in
      setupLoginUser()
      onView(withId(iv_poster)).perform(swipeDown())

      // token is not set, so it should not crash
      token.postValue(NAN)
      onView(withId(iv_poster)).perform(swipeDown())

      // set token to empty string, should not crash
      token.postValue("")
      onView(withId(iv_poster)).perform(swipeDown())
    }
  }

  @Test
  fun swipeRefreshTv_whenScroll_runsCorrectly() {
    context.launchMediaDetailActivity(
      data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)
    ) {
      onView(withId(iv_poster)).perform(swipeDown())
    }
  }

  @Test
  fun swipeRefresh_whenUnknownMediaType_runsCorrectly() {
    context.launchMediaDetailActivity(
      data = testMediaItem.copy(mediaType = "NAN")
    ) {
      onView(withId(iv_poster)).perform(swipeDown())
    }
  }

  @Test
  fun ratingUser_whenClicked_showsDialogRating() {
    context.launchMediaDetailActivity {
      setupLoginUser()

      // submit rating without selecting any rating
      performScoreClick()
      onView(withText(submit)).perform(click())

      // press cancel button
      performScoreClick()
      onView(withText(cancel)).perform(click())

      performScoreClick()
      onView(withId(rating_bar_action)).perform(SetRatingAction(3.5f))
      onView(withText(submit)).perform(click())
    }
  }

  @Test
  fun dialogRatting_submitRatingSuccessful_showsNewUserRating() {
    context.launchMediaDetailActivity(
      data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)
    ) {
      updateState { copy(itemState = testMediaStateRated) }
      setupLoginUser()

      submitRating()

      // shows user rating correctly
      onView(withId(tv_score_your_score)).check(matches(withText("7.0")))
    }
  }

  @Test
  fun listOfGenre_whenClicked_triggersOpenDetailPage() {
    context.launchMediaDetailActivity {
      onView(withId(rv_genre)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
      )
    }

    verify {
      mockNavigator.openList(
        context = any(),
        args = ListArgs(
          listType = BY_GENRE,
          mediaType = testMediaItem.mediaType,
          title = "",
          genreId = testMediaItem.listGenreIds?.get(0) ?: 0,
        )
      )
    }
  }

  @Test
  fun dialogRatting_submitRatingUnsuccessful_notShowsTheRating() {
    context.launchMediaDetailActivity(
      data = testMediaItem.copy(mediaType = TV_MEDIA_TYPE)
    ) {
      updateState { copy(itemState = testMediaStateRated) }
      errorEvent.tryEmit("error")  // ← tryEmit, not emit
      setupLoginUser()

      submitRating()

      onView(withId(tv_score_your_score)).check(matches(not(withText("10.0"))))
    }
  }

  @Test
  fun buttonActionUserLogin_whenClicked_shouldHandlePostData() {
    context.launchMediaDetailActivity {
      setupLoginUser()
      updateState { copy(itemState = testMediaState) } // mock as not favorite and not watchlist

      performClickButtonFavorite()
      performClickButtonWatchlist()
    }
  }

  @Test
  fun itemState_whenNullValue_doNothing() {
    context.launchMediaDetailActivity {
      setupLoginUser()
      updateState { copy(itemState = null) }

      performClickButtonFavorite()
    }
  }

  @Test
  fun buttonActionUserLogin_whenClickedWithMultipleCase_shouldHandlePostData() {
    context.launchMediaDetailActivity {
      setupLoginUser()
      updateState {
        copy(itemState = testMediaState.copy(favorite = false, watchlist = false))
      }

      // post to watchlist success
      performClickButtonWatchlist()
      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = true, isFavorite = false, isDelete = false)
        )
      }

      // delete from watchlist success
      performClickButtonWatchlist()
      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = true, isFavorite = false, isDelete = true)
        )
      }

      // post to watchlist failed
      performClickButtonWatchlist()
      updateState { copy(itemState = null) }

      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = false, isFavorite = false, isDelete = false)
        )
      }

      // post to favorite success
      performClickButtonFavorite()
      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = true, isFavorite = true, isDelete = false)
        )
      }

      // delete from favorite success
      performClickButtonFavorite()
      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = true, isFavorite = true, isDelete = true)
        )
      }

      // post to favorite failed
      performClickButtonFavorite()
      updateState {
        copy(
          mediaStateResult =
            UpdateMediaStateResult(isSuccess = false, isFavorite = true, isDelete = false)
        )
      }

      // post to watchlist when not yet
      updateState {
        copy(isFavorite=false, isWatchlist = false)
      }
      performClickButtonWatchlist()

      // remove from watchlist
      updateState {
        copy(isFavorite=true, isWatchlist = true)
      }
      performClickButtonWatchlist()
    }
  }

  @Test
  fun observeFavoriteWatchlistPost_whenEventAlreadyHandled_shouldNotProcessAgain() {
    context.launchMediaDetailActivity {
      // same event
      val result =
        UpdateMediaStateResult(isSuccess = true, isFavorite = true, isDelete = false)

      setupLoginUser()
      updateState {
        copy(itemState = testMediaState.copy(favorite = false, watchlist = false))
      }

      performClickButtonFavorite()
      updateState { copy(mediaStateResult = result) }
      performClickButtonFavorite()
      updateState { copy(mediaStateResult = result) }
    }
  }

  @Test
  fun buttonWatchlist_whenStateNotInitialized_doNothing() {
    context.launchMediaDetailActivity {
      it.onActivity { activity ->
        activity.userInteractionHandler.resetUserStateForTest()
      }
      performClickButtonWatchlist()
      performClickButtonFavorite()
    }
  }

  private fun performClickButtonFavorite() {
    onView(withId(btn_favorite)).check(matches(isDisplayed())).perform(click())
  }

  private fun performClickButtonWatchlist() {
    onView(withId(btn_watchlist)).check(matches(isDisplayed())).perform(click())
  }

  private fun performScoreClick() {
    onView(withId(score_scrollview)).perform(swipeLeft())
    onView(withId(your_score_viewGroup)).perform(click())
  }

  private fun submitRating() {
    performScoreClick()
    onView(withId(rating_bar_action)).perform(SetRatingAction(5f))
    onView(withText(submit)).perform(click())
  }

  private fun setupLoginUser() {
    token.postValue("test_token")
    region.postValue("US")
  }
}
