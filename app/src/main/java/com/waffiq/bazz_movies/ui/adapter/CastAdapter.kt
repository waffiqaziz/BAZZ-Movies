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
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItemResponse
import com.waffiq.bazz_movies.databinding.ItemCastBinding
import com.waffiq.bazz_movies.ui.activity.person.PersonActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300

class CastAdapter : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

  private val listCast = ArrayList<CastItemResponse>()

  fun setCast(itemStory: List<CastItemResponse>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

  inner class ViewHolder(private var binding: ItemCastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastItemResponse) {
      binding.imgCastPhoto.contentDescription = cast.name

      with(binding) {
        Glide.with(imgCastPhoto)
          .load(TMDB_IMG_LINK_BACKDROP_W300 + cast.profilePath )
          .placeholder(ic_bazz_placeholder_poster)
          .transform(CenterCrop())
          .transition(withCrossFade())
          .error(ic_broken_image)
          .into(imgCastPhoto)

        tvCastName.text = cast.name?: cast.originalName
        tvCastCharacter.text = cast.character
        // image OnClickListener
        container.setOnClickListener {
          val intent = Intent(it.context, PersonActivity::class.java)
          intent.putExtra(PersonActivity.EXTRA_PERSON, cast)
          it.context.startActivity(intent)
        }
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<CastItemResponse>,
    private val newList: List<CastItemResponse>
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