package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
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

  fun refreshData(newList: List<FavoriteDB>, position: Int) {
    listItemDB.clear()
    listItemDB.addAll(newList)
    notifyItemChanged(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listItemDB[position])
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
          if (fav.backDrop?.isNotEmpty() == true) TMDB_IMG_LINK_BACKDROP_W300 + fav.backDrop
          else TMDB_IMG_LINK_POSTER_W185 + fav.poster
        )
        .placeholder(ic_bazz_placeholder_search)
        .error(ic_broken_image)
        .into(binding.ivPicture)

      binding.tvTitle.text = fav.title
      binding.tvGenre.text = fav.genre
      binding.tvYearReleased.text = fav.releaseDate?.let { dateFormatter(it) }

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
      oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldEmployee = oldList[oldItemPosition]
      val newEmployee = newList[newItemPosition]
      return oldEmployee.mediaId == newEmployee.mediaId
    }
  }
}