package com.waffiq.bazz_movies.core.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Utility object providing a function to collect and submit paging data to a PagingDataAdapter.
 * This function is designed to be used within a Fragment to easily handle the collection of paging data
 * and update the adapter with the latest data.
 */
object FlowUtils {

  /**
   * Collects paging data from a provided Flow and submits it to a PagingDataAdapter.
   * This function automatically handles lifecycle states, ensuring that data is collected and submitted
   * when the Fragment's view lifecycle is in the CREATED state.
   *
   * @param fragment The Fragment that will collect the data and update the UI.
   * @param flowProvider A lambda that provides the Flow of PagingData to be collected.
   * @param adapter The PagingDataAdapter that will display the data in the UI.
   */
  fun <T : Any> collectAndSubmitData(
    fragment: Fragment,
    flowProvider: () -> Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>
  ) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
        flowProvider().collectLatest { pagingData ->
          adapter.submitData(fragment.viewLifecycleOwner.lifecycle, pagingData)
        }
      }
    }
  }
}
