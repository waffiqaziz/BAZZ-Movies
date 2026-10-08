package com.waffiq.bazz_movies.feature.list

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeDown
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.list.R.id.btn_back
import com.waffiq.bazz_movies.feature.list.R.id.illustration_error
import com.waffiq.bazz_movies.feature.list.R.id.iv_picture
import com.waffiq.bazz_movies.feature.list.R.id.loading_indicator
import com.waffiq.bazz_movies.feature.list.R.id.option_detailed
import com.waffiq.bazz_movies.feature.list.R.id.option_three_columns
import com.waffiq.bazz_movies.feature.list.R.id.option_two_columns
import com.waffiq.bazz_movies.feature.list.R.id.rv_list
import com.waffiq.bazz_movies.feature.list.domain.model.ListViewMode
import com.waffiq.bazz_movies.feature.list.testutils.BaseListActivityTest
import com.waffiq.bazz_movies.feature.list.ui.ListActivity
import com.waffiq.bazz_movies.feature.list.ui.ViewModeBottomSheet
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ListActivityTest : BaseListActivityTest() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockListViewModel: ListViewModel = mockk(relaxed = true)

  @Before
  override fun setup() {
    super.setup()
    hiltRule.inject()
    setupMock(mockListViewModel)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun listActivity_toggleButtonPressed_changesTheLayout() {
    context.launchListActivity {
      // should use grid layout on initial
      "movie title 1".doesNotExist()

      // switch to detail layout
      triggerListButton(option_detailed)
      "movie title 1".isVisible()

      // back to grid layout
      triggerListButton(option_two_columns)
      "movie title 1".doesNotExist()

      // grid layout 3
      triggerListButton(option_three_columns)
      "movie title 1".doesNotExist()
    }
  }

  @Test
  fun listActivity_afterRecreate_restoresViewMode() {
    context.launchListActivity {
      triggerListButton(option_detailed)
    }
    context.launchListActivityAndRecreate {
      "movie title 1".doesNotExist()
    }
  }

  @Test
  fun listActivity_recreate_restoresSavedViewMode() {
    launch<ListActivity>(intent).use { scenario ->
      scenario.onActivity { activity ->
        activity.supportFragmentManager.setFragmentResult(
          ViewModeBottomSheet.REQUEST_KEY,
          Bundle().apply {
            putString(
              ViewModeBottomSheet.RESULT_MODE,
              ListViewMode.THREE_COLUMNS.name,
            )
          },
        )
      }

      scenario.recreate()

      scenario.onActivity { activity ->
        val rv = activity.findViewById<RecyclerView>(rv_list)
        val lm = rv.layoutManager as GridLayoutManager
        assertEquals(ListViewMode.THREE_COLUMNS.spanCount, lm.spanCount)
      }
    }
  }

  @Test
  fun viewMode_resultWithoutMode_isIgnored() {
    launch<ListActivity>(intent).use { scenario ->
      scenario.selectViewMode(ListViewMode.THREE_COLUMNS)
      scenario.selectViewMode(ListViewMode.THREE_COLUMNS) // trigger same view mode
      scenario.sendEmptyViewModeResult()
      scenario.assertLayout(ListViewMode.THREE_COLUMNS)
    }
  }

  @Test
  fun swipeRefreshMovie_whenScroll_runsWithoutProblem() {
    context.launchListActivity {
      rv_list.performSwipeDown()
      shortDelay()
      rv_list.performSwipeDown()
    }
  }

  @Test
  fun onKeywordsLoadState_itemNotZero_showsBackdrop() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.loadStateChanged()
      }
    }
  }

  @Test
  fun onKeywordsLoadState_withEmptyData_stillShowsImageView() {
    every { mockListViewModel.getByKeyword(any(), any()) } returns flowOf(PagingData.empty())

    context.launchListActivity(movieKeywordsArgs) { scenario ->
      iv_picture.isDisplayed() // image view shows but not the actual backdrop
      scenario.onActivity { activity ->
        activity.loadStateChanged()
      }
    }
  }

  @Test
  fun buttonClose_whenClicked_finishTheActivity() {
    context.launchListActivity { scenario ->
      btn_back.performClick()

      // check if the activity is finished
      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun activity_whenStateIsLoading_showsIndicatorAndHidesList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Loading)
      }
      loading_indicator.isDisplayed()
      rv_list.isNotDisplayed()
      illustration_error.isNotDisplayed()
    }
  }

  @Test
  fun activity_successState_showsList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Success(Unit))
      }
      rv_list.isDisplayed()
      loading_indicator.isNotDisplayed()
    }
  }

  @Test
  fun activity_onErrorState_showsRetryAndHidesList() {
    context.launchListActivity(movieKeywordsArgs) { scenario ->
      scenario.onActivity { activity ->
        activity.handleRefreshState(UIState.Error("Something went wrong"))
      }
      illustration_error.isDisplayed()
      rv_list.isNotDisplayed()
      btn_try_again.performClick()
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullListActivity(args = null) { scenario ->
      assertTrue(scenario.state == Lifecycle.State.DESTROYED)
    }
  }
}
