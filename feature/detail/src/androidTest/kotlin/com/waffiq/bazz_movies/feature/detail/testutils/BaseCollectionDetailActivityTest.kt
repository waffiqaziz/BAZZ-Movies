package com.waffiq.bazz_movies.feature.detail.testutils

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.CollectionSortOption
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.genreIds
import com.waffiq.bazz_movies.feature.detail.testutils.DataDumb.detailCollections
import com.waffiq.bazz_movies.feature.detail.ui.CollectionDetailActivity
import com.waffiq.bazz_movies.feature.detail.ui.state.CollectionUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before

/**
 * Test helper for [CollectionDetailActivity] that provides mock implementations
 */
abstract class BaseCollectionDetailActivityTest {

  protected lateinit var context: Context

  protected val uiState = MutableStateFlow(CollectionUiState())
  protected val sUiState = uiState.asStateFlow()

  @Before
  open fun setup() {
    Intents.init()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  protected fun setupBaseMocks(mockCollectionViewModel: CollectionViewModel) {
    every { mockCollectionViewModel.uiState } returns sUiState
    every { mockCollectionViewModel.currentSort } returns
      MutableStateFlow(CollectionSortOption.RELEASE_DATE_OLDEST)
    every { mockCollectionViewModel.loadMovieCollection(any()) } just Runs
    every { mockCollectionViewModel.applySort(any()) } just Runs

    @Suppress("UNCHECKED_CAST")
    uiState.update {
      it.copy(
        isLoading = false,
        isError = false,
        name = detailCollections.name.orEmpty(),
        overview = detailCollections.overview.orEmpty(),
        genreIds = detailCollections.genreIds,
        backdropUrl = detailCollections.backdropPath,
        parts = detailCollections.parts as List<PartsItem>,
      )
    }
  }

  protected fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openList(any(), any()) } just Runs
  }

  protected fun initializeTest(context: Context) {
    this.context = context
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      Glide.get(context).clearMemory()
    }
  }

  protected fun Context.launchCollectionDetailActivity(
    block: (ActivityScenario<CollectionDetailActivity>) -> Unit,
  ) {
    this.launchCollectionDetailActivity(22) { block(it) }
  }

  protected fun Context.launchCollectionDetailActivity(
    collectionId: Int,
    block: (ActivityScenario<CollectionDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, CollectionDetailActivity::class.java).apply {
      putExtra(CollectionDetailActivity.EXTRA_COLLECTION_ID, collectionId)
    }

    ActivityScenario.launch<CollectionDetailActivity>(intent).use { scenario ->
      scenario.onActivity { /* do nothing */ }
      shortDelay()
      block(scenario)
    }
  }

  protected fun Context.launchNullCollectionDetailActivity(
    collectionId: Int?,
    block: (ActivityScenario<CollectionDetailActivity>) -> Unit,
  ) {
    val intent = Intent(this, CollectionDetailActivity::class.java).apply {
      collectionId?.let {
        putExtra(CollectionDetailActivity.EXTRA_COLLECTION_ID, it)
      }
    }

    ActivityScenario.launch<CollectionDetailActivity>(intent).use { scenario ->
      block(scenario)
    }
  }

  protected fun updateState(block: CollectionUiState.() -> CollectionUiState) =
    uiState.update { it.block() }
}
