package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.databinding.ItemPlayForBinding
import com.waffiq.bazz_movies.domain.model.person.CastItem
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W185

class KnownForAdapter : RecyclerView.Adapter<KnownForAdapter.ViewHolder>() {

  private val listCast = ArrayList<CastItem>()

  fun setCast(itemStory: List<CastItem>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemPlayForBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemPlayForBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastItem) {
      binding.imgCastPhoto.contentDescription = cast.title

      Glide.with(binding.imgCastPhoto)
        .load(TMDB_IMG_LINK_POSTER_W185 + cast.posterPath)
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgCastPhoto)

      binding.tvCastName.text = cast.name ?: cast.title ?: cast.originalName ?: cast.originalTitle
      binding.tvCastCharacter.text = cast.character ?: "N/A"

      val resultItemResponse = ResultItemResponse(
        overview = cast.overview,
        title = cast.title,
        name = cast.name,
        originalTitle = cast.originalTitle,
        originalName = cast.originalTitle,
        mediaType = cast.mediaType,
        firstAirDate = cast.releaseDate,
        releaseDate = cast.releaseDate,
        genreIds = cast.genreIds,
        id = cast.id,
        popularity = cast.popularity,
        voteAverage = cast.voteAverage,
        voteCount = cast.voteCount,
        posterPath = cast.posterPath,
        backdropPath = cast.backdropPath,
      )

      // OnClickListener
      binding.container.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItemResponse)
        it.context.startActivity(intent)
      }

    }
  }

  inner class DiffCallback(
    private val oldList: List<CastItem>,
    private val newList: List<CastItem>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldEmployee = oldList[oldItemPosition]
      val newEmployee = newList[newItemPosition]
      return oldEmployee.id == newEmployee.id
    }
  }
}