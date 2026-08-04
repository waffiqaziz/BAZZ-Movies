package com.waffiq.bazz_movies.feature.search.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.common.value
import com.waffiq.bazz_movies.core.designsystem.R.array.genre_tile_colors
import com.waffiq.bazz_movies.feature.search.databinding.ItemGenreListBinding
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.iconRes
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType.BY_GENRE
import com.waffiq.bazz_movies.navigation.MediaSource
import kotlin.math.absoluteValue

class GenreAdapter(private val navigator: INavigator) :
  ListAdapter<Genre, GenreAdapter.ViewHolder>(DIFF) {

  private var mediaType = MediaType.MOVIE

  fun setMediaType(mediaType: MediaType) {
    this.mediaType = mediaType
  }

  inner class ViewHolder(private val binding: ItemGenreListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(genre: Genre) {
      val context = binding.root.context
      val colors = context.resources.obtainTypedArray(genre_tile_colors)
      val index = genre.genreName.hashCode().absoluteValue % colors.length()
      val color = colors.getColor(index, Color.DKGRAY)
      colors.recycle()

      binding.cardGenre.setCardBackgroundColor(color)
      binding.tvGenreName.text = genre.genreName
      binding.ivGenreIcon.setImageResource(genre.iconRes())
      binding.root.setOnClickListener {
        navigator.openList(
          itemView.context,
          args = ListArgs(
            listType = BY_GENRE,
            mediaType = MediaSource.Typed(mediaType.value),
            title = "", // empty for genre
            id = genre.id,
          ),
        )
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemGenreListBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false,
    )
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  companion object {
    private val DIFF = object : DiffUtil.ItemCallback<Genre>() {
      override fun areItemsTheSame(old: Genre, new: Genre) = old.id == new.id

      // Genre is an immutable enum, so items with the same ID always have the same content.
      override fun areContentsTheSame(old: Genre, new: Genre) = true
    }
  }
}
