package com.waffiq.bazz_movies.feature.detail.ui

import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.id.btn_try_again
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.R.id.btn_back
import com.waffiq.bazz_movies.feature.detail.R.id.rv_collection_parts
import com.waffiq.bazz_movies.feature.detail.R.id.rv_genre
import com.waffiq.bazz_movies.feature.detail.R.id.tv_movies
import com.waffiq.bazz_movies.feature.detail.testutils.BaseCollectionDetailActivityTest
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test for [CollectionDetailActivity] that checks the visibility of UI elements
 * and the correct display of media details based on various conditions.
 */
@HiltAndroidTest
class CollectionDetailActivityTest : BaseCollectionDetailActivityTest() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockCollectionViewModel: CollectionViewModel = mockk(relaxed = true)

  @Before
  override fun setup() {
    hiltRule.inject()
    setupBaseMocks(mockCollectionViewModel)
    setupNavigatorMocks(mockNavigator)
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @Test
  fun detailScreen_whenAllDataProvided_showsAllViews() {
    context.launchCollectionDetailActivity {
      btn_back.isDisplayed()
      rv_genre.isDisplayed()
      tv_movies.isDisplayed()
      rv_collection_parts.isDisplayed()
    }
  }

  @Test
  fun detailScreen_whenError_showsErrorViews() {
    context.launchCollectionDetailActivity {
      uiState.update { it.copy(name = "", isError = false, isLoading = false) }
      shortDelay()

      uiState.update { it.copy(name = "", isError = true, isLoading = true) }
      shortDelay()

      uiState.update { it.copy(isError = true) }
      shortDelay()

      uiState.update { it.copy(name = "something", isError = true, isLoading = false) }
      shortDelay()

      btn_back.isDisplayed()
      rv_genre.isNotDisplayed()
      tv_movies.isNotDisplayed()
      rv_collection_parts.isNotDisplayed()

      btn_try_again.performClick()
      verify { mockCollectionViewModel.loadMovieCollection(any()) }
    }
  }

  @Test
  fun buttonBack_whenClicked_finishTheActivity() {
    context.launchCollectionDetailActivity { scenario ->
      btn_back.performClick()

      scenario.moveToState(DESTROYED)
      assertTrue(scenario.state == DESTROYED)
    }
  }

  @Test
  fun extractDataFromIntent_returnsFalse_whenExtraMissing() {
    context.launchNullCollectionDetailActivity(null) { scenario ->
      assertTrue(scenario.state == DESTROYED)
    }
  }
}
