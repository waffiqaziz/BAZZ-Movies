package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_GENRE

class GenreAdapter(private val navigator: INavigator) :
  RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

  private var genreIdList = emptyList<Int>()
  private var mediaType = MOVIE_MEDIA_TYPE

  fun setMediaType(type: String) {
    mediaType = type
  }

  @SuppressLint("NotifyDataSetChanged")
  fun setGenre(newList: List<Int>) {
    genreIdList = newList
    notifyDataSetChanged() // fine due to small data under 1-5 items
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ChipGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(genreIdList[position])
  }

  override fun getItemCount(): Int = genreIdList.size

  inner class ViewHolder(private val binding: ChipGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(id: Int) {
      binding.chip.text = getGenreName(id)

      // open list
      binding.chip.setOnClickListener {
        navigator.openList(
          context = itemView.context,
          args = ListArgs(
            listType = BY_GENRE,
            mediaType = mediaType,
            title = "", // empty for genre
            id = id,
          ),
        )
      }
    }
  }
}
