package com.waffiq.bazz_movies.utils.helpers

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object FlowUtils {
  fun <T : Any> collectAndSubmitData(
    fragment: Fragment,
    flowProvider: () -> Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>
  ) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
      flowProvider().collectLatest { pagingData ->
        adapter.submitData(fragment.viewLifecycleOwner.lifecycle, pagingData)
      }
    }
  }

  fun <T : Any> collectAndSubmitDataJob(
    fragment: Fragment,
    flowProvider: () -> Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>
  ): Job {
    return fragment.viewLifecycleOwner.lifecycleScope.launch {
      flowProvider().collectLatest { pagingData ->
        adapter.submitData(fragment.viewLifecycleOwner.lifecycle, pagingData)
      }
    }
  }
}