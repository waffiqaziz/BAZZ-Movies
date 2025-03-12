package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemLoadingBinding

/**
 * An adapter class that handles the display of loading states in a RecyclerView.
 * It provides functionality to show a loading indicator, an error message, and a retry button when
 * loading data or encountering errors during data loading.
 *
 * This adapter uses the `LoadStateAdapter` to manage the states of loading, error, and retry.
 * It is used in scenarios where pagination or network loading is involved.
 */
class LoadingStateAdapter(
  private val retry: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

  /**
   * Creates a new [LoadingStateViewHolder] that binds a layout for displaying loading or error states.
   *
   * @param parent The parent [ViewGroup] that holds the view.
   * @param loadState The current state of the loading operation.
   * @return A [LoadingStateViewHolder] with the inflated layout and retry functionality.
   */
  override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
    val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return LoadingStateViewHolder(binding, retry)
  }

  /**
   * Binds the given [loadState] to the [LoadingStateViewHolder], updating the view based on the loading state.
   *
   * @param holder The [LoadingStateViewHolder] that holds the views.
   * @param loadState The current state of the loading operation, such as loading, error, or success.
   */
  override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
    holder.bind(loadState)
  }

  /**
   * A ViewHolder class that binds the layout to represent the loading, error, and retry states.
   * It includes a retry button that triggers the retry logic when clicked.
   */
  inner class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    init {
      // sets up the retry button to invoke the retry logic when clicked.
      binding.retryButton.setOnClickListener { retry.invoke() }
    }

    /**
     * Binds the current [loadState] to the views in the layout.
     * If an error state is encountered, it displays the error message and shows the retry button.
     *
     * @param loadState The current state of the loading process, which could be loading, error, or success.
     */
    fun bind(loadState: LoadState) {
      if (loadState is LoadState.Error) {
        binding.errorMsg.text = loadState.error.message
      }
      // Uncomment to show a progress bar during loading state
      // binding.progressBar.isVisible = loadState is LoadState.Loading
      binding.retryButton.isVisible = loadState is LoadState.Error
      binding.errorMsg.isVisible = loadState is LoadState.Error
    }
  }
}
