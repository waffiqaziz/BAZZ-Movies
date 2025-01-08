package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemLoadingBinding
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoadingStateAdapterTest {

  private val retryMock: () -> Unit = mockk(relaxed = true)
  private lateinit var context: Context
  private lateinit var adapter: LoadingStateAdapter
  private lateinit var binding: ItemLoadingBinding
  private lateinit var inflater: LayoutInflater
  private lateinit var viewHolder: LoadingStateAdapter.LoadingStateViewHolder

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    adapter = LoadingStateAdapter(retryMock)
    inflater = LayoutInflater.from(context)
    binding = ItemLoadingBinding.inflate(inflater, null, false)
    viewHolder = LoadingStateAdapter.LoadingStateViewHolder(binding, retryMock)
  }

  @Test
  fun `onCreateViewHolder creates the correct view holder`() {
    val parent = FrameLayout(context)
    val loadState = LoadState.Loading
    val viewHolder = adapter.onCreateViewHolder(parent, loadState)

    assertNotNull(viewHolder)
  }

  @Test
  fun `onBindViewHolder binds the correct error state to the view holder`() {
    val errorState = LoadState.Error(Throwable("Test error message"))
    viewHolder.bind(errorState)
    assertEquals("Test error message", binding.errorMsg.text.toString())
    assertEquals(true, binding.retryButton.isVisible)
    assertEquals(true, binding.errorMsg.isVisible)
  }

  @Test
  fun `onBindViewHolder binds the correct loading state to the view holder`() {
    val loadingState = LoadState.Loading
    viewHolder.bind(loadingState)
    assertEquals(false, binding.retryButton.isVisible)
    assertEquals(false, binding.errorMsg.isVisible)
  }

  @Test
  fun `state transitions from error to loading update UI correctly`() {
    // start with error state
    val errorState = LoadState.Error(Throwable("Error message"))
    viewHolder.bind(errorState)
    assertEquals(true, binding.retryButton.isVisible)
    assertEquals(true, binding.errorMsg.isVisible)
    assertEquals("Error message", binding.errorMsg.text.toString())

    // transition to loading state
    val loadingState = LoadState.Loading
    viewHolder.bind(loadingState)
    assertEquals(false, binding.retryButton.isVisible)
    assertEquals(false, binding.errorMsg.isVisible)
  }

  @Test
  fun `state transitions from loading to error update UI correctly`() {
    // start from loading state
    val loadingState = LoadState.Loading
    viewHolder.bind(loadingState)
    assertEquals(false, binding.retryButton.isVisible)
    assertEquals(false, binding.errorMsg.isVisible)

    // transition to error state
    val errorState = LoadState.Error(Throwable("Error message"))
    viewHolder.bind(errorState)
    assertEquals(true, binding.retryButton.isVisible)
    assertEquals(true, binding.errorMsg.isVisible)
    assertEquals("Error message", binding.errorMsg.text.toString())
  }

  @Test
  fun `retry button is hidden for non-error states`() {
    val notLoadingState = LoadState.NotLoading(endOfPaginationReached = true)
    viewHolder.bind(notLoadingState)

    assertEquals(false, binding.retryButton.isVisible)
    assertEquals(false, binding.errorMsg.isVisible)
  }

  @Test
  fun `error message handles long text`() {
    val longMessage = "This is a very long error message that exceeds normal limits."
    val errorState = LoadState.Error(Throwable(longMessage))
    viewHolder.bind(errorState)

    assertEquals(longMessage, binding.errorMsg.text.toString())
    assertEquals(true, binding.retryButton.isVisible)
  }

  @Test
  fun `retry button invokes the retry callback`() {
    binding.retryButton.performClick() // simulate button click

    verify { retryMock.invoke() }
  }

  @Test
  fun `retry button click without listener`() {
    val bindingWithoutListener = ItemLoadingBinding.inflate(inflater, null, false)
    bindingWithoutListener.retryButton.performClick()

    assertNotNull(bindingWithoutListener.retryButton)
  }

  @Test
  fun `adapter handles empty state`() {
    val itemCount = adapter.itemCount
    assertEquals(0, itemCount) // initially, there should be no items
  }

  @Test
  fun `UI visibility edge cases when error is null`() {
    val errorState = LoadState.Error(Throwable(null as String?))
    viewHolder.bind(errorState)

    assertEquals("", binding.errorMsg.text.toString()) // textView should show empty text
    assertEquals(true, binding.retryButton.isVisible)
    assertEquals(true, binding.errorMsg.isVisible)
  }
}
