package com.waffiq.bazz_movies.feature.search.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.waffiq.bazz_movies.core.models.SearchHistory

class HistoryDiffUtil : DiffUtil.ItemCallback<SearchHistory>() {
  override fun areItemsTheSame(old: SearchHistory, new: SearchHistory) = old.query == new.query
  override fun areContentsTheSame(old: SearchHistory, new: SearchHistory) = old == new
}
