package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.databinding.ItemResultBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.utils.Helper.dateFormatter

class FavoriteAdapterDB : RecyclerView.Adapter<FavoriteAdapterDB.ViewHolder>() {

  private val listItemDB = ArrayList<FavoriteDB>()

  fun setFavorite(itemStory: List<FavoriteDB>) {
    val diffCallback = DiffCallback(this.listItemDB, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listItemDB.clear()
    this.listItemDB.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listItemDB[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount() = listItemDB.size

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: FavoriteDB

    fun bind(fav: FavoriteDB) {
      data = fav
      binding.ivPicture.contentDescription = fav.title

      Glide.with(binding.ivPicture)
        .load(
          if (fav.backDrop != null) TMDB_IMG_LINK_BACKDROP_W300 + fav.backDrop
          else if (fav.poster != null) TMDB_IMG_LINK_POSTER_W185 + fav.poster
          else ic_backdrop_error
        )
        .placeholder(ic_bazz_placeholder_search)
        .error(ic_backdrop_error)
        .into(binding.ivPicture)

      binding.tvTitle.text = fav.title
      binding.tvGenre.text = fav.genre
      binding.tvYearReleased.text = fav.releaseDate?.let { dateFormatter(it) } ?: "N/A"

      val resultItem = ResultItem(
        backdropPath = fav.backDrop,
        posterPath = fav.poster,
        releaseDate = fav.releaseDate,
        overview = fav.overview,
        title = fav.title,
        originalTitle = fav.title,
        mediaType = fav.mediaType,
        id = fav.mediaId
      )

      binding.containerResult.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
        it.context.startActivity(intent)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<FavoriteDB>,
    private val newList: List<FavoriteDB>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      (oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId
        && oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite
        && oldList[oldItemPosition].isWatchlist == newList[newItemPosition].isWatchlist
        && oldList[oldItemPosition].mediaType == newList[newItemPosition].mediaType)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      (oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId
        && oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite
        && oldList[oldItemPosition].isWatchlist == newList[newItemPosition].isWatchlist
        && oldList[oldItemPosition].mediaType == newList[newItemPosition].mediaType)
  }
}