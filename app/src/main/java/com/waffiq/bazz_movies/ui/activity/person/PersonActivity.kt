package com.waffiq.bazz_movies.ui.activity.person

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.dateFormater
import com.waffiq.bazz_movies.utils.Helper.getAgeBirth
import com.waffiq.bazz_movies.utils.Helper.getAgeDeath

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
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_PERSON)!!
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
    personMovieViewModel.getImagePerson().observe(this) { adapterImage.setCast(it) }

    // show detail person
    personMovieViewModel.getDetailPerson(dataExtra.id!!)
    personMovieViewModel.getDetailPerson().observe(this) {

      binding.tvName.text = it.name
      binding.tvBiography.text = it.biography
      showBirthdate(it)

      // show photo
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
    }, 800)
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }
  }

  private fun animFadeOut() {
    val animation = animFadeOutLong(this)
    binding.backgroundDimPerson.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimPerson.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, 600)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimPerson.visibility = View.VISIBLE // blur background when loading
      binding.progressBar.visibility = View.VISIBLE
    } else animFadeOut()
  }

  private fun showBirthdate(it: DetailPersonResponse) {
    if (it.deathday.isNullOrEmpty()) {
      binding.tvDeath.isVisible = false
      binding.tvDeadHeader.isVisible = false

      val birthday = "${it.birthday?.let { dateFormater(it) }} (${
        it.birthday?.let { getAgeBirth(it) }
      } ${getString(R.string.years_old)}) \n${it.placeOfBirth}"
      binding.tvBorn.text = birthday

    } else {
      binding.tvDeath.isVisible = true
      binding.tvDeadHeader.isVisible = true

      val birthDay = "${it.birthday?.let { dateFormater(it) }} \n${it.placeOfBirth}"
      binding.tvBorn.text = birthDay
      val deathDay = "${dateFormater(it.deathday)} (${
        getAgeDeath(
          it.birthday!!,
          it.deathday
        )
      } ${getString(R.string.years_old)})"
      binding.tvDeath.text = deathDay

    }
  }

  companion object {
    const val EXTRA_PERSON = "EXTRA_PERSON"
  }
}