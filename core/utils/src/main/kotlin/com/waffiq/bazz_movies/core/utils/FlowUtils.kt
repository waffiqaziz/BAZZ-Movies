package com.waffiq.bazz_movies.core.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Utility object providing a function to collect and submit paging data to a PagingDataAdapter.
 */
object FlowUtils {

  fun <T : Any> collectAndSubmitData(
    fragment: Fragment,
    flowProvider: () -> Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>,
  ) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        flowProvider().collectLatest { pagingData ->
          adapter.submitData(fragment.viewLifecycleOwner.lifecycle, pagingData)
        }
      }
    }
  }

  fun <T : Any> collectAndSubmitData(
    lifecycleOwner: LifecycleOwner,
    flowProvider: () -> Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>,
  ) {
    lifecycleOwner.lifecycleScope.launch {
      lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        flowProvider().collectLatest { pagingData ->
          adapter.submitData(lifecycleOwner.lifecycle, pagingData)
        }
      }
    }
  }
}
