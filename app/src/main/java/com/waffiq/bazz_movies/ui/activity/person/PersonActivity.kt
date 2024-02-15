package com.waffiq.bazz_movies.ui.activity.person

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.dateFormater
import com.waffiq.bazz_movies.utils.Helper.getAgeBirth
import com.waffiq.bazz_movies.utils.Helper.getAgeDeath
import com.waffiq.bazz_movies.utils.Helper.showToastShort

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
        intent.getParcelableExtra(EXTRA_PERSON, CastItem::class.java)!!
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_PERSON)!!
      }
    } else finish()

    binding.swipeRefresh.setOnRefreshListener {
      val i = Intent(this, PersonActivity::class.java)
      i.putExtra(EXTRA_PERSON, dataExtra)
      overridePendingTransition(0, 0)
      binding.swipeRefresh.isRefreshing = false
      startActivity(i)
      overridePendingTransition(0, 0)
      finish()
    }
  }

  private fun showData() {
    personMovieViewModel.getLoading().observe(this) { showLoading(it) }
    personMovieViewModel.getSnackbar().observe(this) { showSnackBarWarning(it) }

    // setup recycle view and adapter
    binding.rvKnownFor.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterKnownFor = KnownForAdapter()
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterImage = ImagePersonAdapter()
    binding.rvPhotos.adapter = adapterImage

    binding.tvName.text = dataExtra.name ?: dataExtra.originalName
    Glide.with(binding.ivPicture)
      .load(Constants.TMDB_IMG_LINK_POSTER_W500 + dataExtra.profilePath)
      .placeholder(R.drawable.ic_bazz_placeholder_poster)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivPicture)

    // show known for
    personMovieViewModel.getKnownFor(dataExtra.id!!)
    personMovieViewModel.getKnownFor().observe(this) { adapterKnownFor.setCast(it) }

    // show picture
    personMovieViewModel.getImagePerson(dataExtra.id!!)
    personMovieViewModel.getImagePerson().observe(this) { adapterImage.setImage(it) }
    showToastShort(this, adapterImage.itemCount.toString())
//    if (adapterImage.itemCount == 0 ){
//      binding.rvPhotos.visibility = View.GONE
//      binding.tvPhotosHeader.visibility = View.GONE
//    } else{
//      binding.rvPhotos.visibility = View.VISIBLE
//      binding.tvPhotosHeader.visibility = View.VISIBLE
//    }

    // show detail person
    personMovieViewModel.getDetailPerson(dataExtra.id!!)
    personMovieViewModel.getDetailPerson().observe(this) {
      if (it.birthday != null)
        if (it.birthday.isNotBlank() && it.birthday.isNotEmpty()) binding.tvBiography.text =
          it.biography
        else binding.tvBiography.text = getString(R.string.no_biography)
      else binding.tvBiography.text = getString(R.string.no_biography)
      showBirthdate(it)
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

      if (it.birthday != null)
        if (it.birthday.isNotEmpty() && it.birthday.isNotBlank()) {
          val birthday = "${it.birthday.let { dateFormater(it) }} (${
            it.birthday.let { getAgeBirth(it) }
          } ${getString(R.string.years_old)}) \n${it.placeOfBirth}"
          binding.tvBorn.text = birthday
        } else binding.tvBorn.text = getString(R.string.no_data)
      else binding.tvBorn.text = getString(R.string.no_data)

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

  private fun showSnackBarWarning(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    val snackBar = Snackbar.make(
      binding.constraintLayout,
      message,
      Snackbar.LENGTH_SHORT
    )

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(
      ContextCompat.getColor(
        this,
        R.color.red_matte
      )
    )
    snackBar.show()
  }

  companion object {
    const val EXTRA_PERSON = "EXTRA_PERSON"
  }
}