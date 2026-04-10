package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getListOfKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toValidKeywordOrNull
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_KEYWORD

class KeywordsAdapter(private val navigator: INavigator) :
  ListAdapter<MediaKeywordsItem, KeywordsAdapter.ViewHolder>(CastDiffCallback()) {

  private var mediaType = MOVIE_MEDIA_TYPE

  fun setMediaType(type: String) {
    mediaType = type
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ChipGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun submitList(list: List<MediaKeywordsItem?>?) {
    super.submitList(getListOfKeywords(list))
  }

  inner class ViewHolder(private var binding: ChipGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: MediaKeywordsItem) {
      val keyword = data.toValidKeywordOrNull() ?: return

      binding.chip.text = keyword.name
      binding.chip.setOnClickListener {
        navigator.openList(
          context = itemView.context,
          args = ListArgs(
            listType = BY_KEYWORD,
            mediaType = mediaType,
            title = keyword.name,
            id = keyword.id,
          ),
        )
      }
    }
  }

  class CastDiffCallback : DiffUtil.ItemCallback<MediaKeywordsItem>() {
    override fun areItemsTheSame(oldItem: MediaKeywordsItem, newItem: MediaKeywordsItem): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(
      oldItem: MediaKeywordsItem,
      newItem: MediaKeywordsItem,
    ): Boolean = oldItem.id == newItem.id
  }
}
