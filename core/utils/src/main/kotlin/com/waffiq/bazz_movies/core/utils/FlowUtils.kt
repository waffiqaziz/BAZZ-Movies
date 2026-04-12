package com.waffiq.bazz_movies.core.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.cachedIn
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
    val cachedFlow = flowProvider().cachedIn(fragment.viewLifecycleOwner.lifecycleScope)

    fragment.viewLifecycleOwner.lifecycleScope.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        cachedFlow.collectLatest { pagingData ->
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
    val cachedFlow = flowProvider().cachedIn(lifecycleOwner.lifecycleScope)

    lifecycleOwner.lifecycleScope.launch {
      lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        cachedFlow.collectLatest { pagingData ->
          adapter.submitData(pagingData)
        }
      }
    }
  }

  fun <T : Any> AppCompatActivity.load(
    flow: Flow<PagingData<T>>,
    adapter: PagingDataAdapter<T, *>,
  ) {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        flow.collectLatest(adapter::submitData)
      }
    }
  }

  fun <T> LifecycleOwner.collectFlow(
    flow: Flow<T>,
    collectLatest: Boolean = true,
    block: suspend (T) -> Unit,
  ) {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        if (collectLatest) {
          flow.collectLatest(block)
        } else {
          flow.collect(block)
        }
      }
    }
  }

  fun <T : Any> LifecycleOwner.collectPagingData(
    flow: Flow<PagingData<T>>,
    block: suspend (PagingData<T>) -> Unit,
  ) {
    val cachedFlow = flow.cachedIn(lifecycleScope)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        cachedFlow.collectLatest(block)
      }
    }
  }
}
