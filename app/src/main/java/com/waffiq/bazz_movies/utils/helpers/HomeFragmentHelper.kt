package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.R.string.data
import com.waffiq.bazz_movies.R.string.no_data
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import com.waffiq.bazz_movies.utils.common.Constants.DEBOUNCE_TIME
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale

object HomeFragmentHelper {
  fun handleLoadState(
    context: Context,
    adapter: PagingDataAdapter<*, *>,
    recyclerView: RecyclerView,
    textView: TextView,
    noMoviesStringRes: Int,
    region: String,
    lifecycleOwner: LifecycleOwner
  ){
    lifecycleOwner.lifecycleScope.launch {
      @OptIn(FlowPreview::class)
      adapter.loadStateFlow.debounce(DEBOUNCE_TIME).distinctUntilChanged().collectLatest {loadState ->
        if (loadState.source.refresh is LoadState.NotLoading &&
          loadState.append.endOfPaginationReached &&
          adapter.itemCount < 1
        ) {
          showToastShort(
            context,
            context.getString(noMoviesStringRes, Locale("", region).displayCountry)
          )
          recyclerView.isGone = true
          if (!textView.text.contains(context.getString(data))) {
            textView.append(" (${context.getString(no_data)})")
          }
        }
      }
    }
  }
}
