package com.waffiq.bazz_movies.ui.activity.person

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants

class PersonActivity : AppCompatActivity() {

  private lateinit var binding: ActivityPersonBinding
  private lateinit var dataExtra: CastItem
  private lateinit var personMovieViewModel: PersonMovieViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPersonBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelFactory.getInstance(this)
    personMovieViewModel = ViewModelProvider(this, factory)[PersonMovieViewModel::class.java]

    getDataExtra()
    showData()
    btnListener()
  }

  private fun getDataExtra() {
    // check if intent hasExtra
    if (intent.hasExtra(EXTRA_PERSON)) {
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra("EXTRA_PERSON", CastItem::class.java)!!
      } else {
        @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_PERSON)!!
      }
    } else finish()
  }

  private fun showData() {
    personMovieViewModel.getLoading().observe(this) { showLoading(it) }

    // setup recycle view and adapter
    binding.rvKnownFor.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterKnownFor = KnownForAdapter()
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterImage = ImagePersonAdapter()
    binding.rvPhotos.adapter = adapterImage

    // show known for
    personMovieViewModel.getKnownFor(dataExtra.id!!)
    personMovieViewModel.getKnownFor().observe(this) { adapterKnownFor.setCast(it) }

    // show picture
    personMovieViewModel.getImagePerson(dataExtra.id!!)
    personMovieViewModel.getImagePerson().observe(this){  adapterImage.setCast(it)  }

    // show detail person
    personMovieViewModel.getDetailPerson(dataExtra.id!!)
    personMovieViewModel.getDetailPerson().observe(this) {
      val birthday = "${it.birthday} \n ${it.placeOfBirth}"

      binding.tvName.text = it.name
      binding.tvBiography.text = it.biography
      binding.tvBorn.text = birthday

      Glide.with(binding.ivPicture)
        .load(
          if (dataExtra.profilePath.isNullOrEmpty()) {
            Constants.TMDB_IMG_LINK_POSTER_W500 + it.profilePath
          } else {
            Constants.TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.profilePath
          }
        ) // URL movie poster
        .placeholder(R.drawable.ic_bazz_placeholder_poster)
        .error(R.drawable.ic_broken_image)
        .into(binding.ivPicture)
    }

    Handler(Looper.getMainLooper()).postDelayed({
      binding.tvBiography.performClick() // set automatic click
    }, 500)
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDim.visibility = View.VISIBLE // blur background when loading
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.backgroundDim.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }
  }

  companion object {
    const val EXTRA_PERSON = "EXTRA_PERSON"
  }
}