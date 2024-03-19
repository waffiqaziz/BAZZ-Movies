package com.waffiq.bazz_movies.ui.activity.person

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.id.btn_close_dialog
import com.waffiq.bazz_movies.R.id.view_page_dialog
import com.waffiq.bazz_movies.R.layout.dialog_image
import com.waffiq.bazz_movies.R.string.no_biography
import com.waffiq.bazz_movies.R.string.no_data
import com.waffiq.bazz_movies.R.string.years_old
import com.waffiq.bazz_movies.data.remote.response.tmdb.CastItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.ui.adapter.ImagePagerAdapter
import com.waffiq.bazz_movies.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants.FACEBOOK_LINK
import com.waffiq.bazz_movies.utils.Constants.IMDB_PERSON_LINK
import com.waffiq.bazz_movies.utils.Constants.INSTAGRAM_LINK
import com.waffiq.bazz_movies.utils.Constants.TIKTOK_PERSON_LINK
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.utils.Constants.WIKIDATA_PERSON_LINK
import com.waffiq.bazz_movies.utils.Constants.X_LINK
import com.waffiq.bazz_movies.utils.Constants.YOUTUBE_CHANNEL_LINK
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.dateFormatter
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
        intent.getParcelableExtra(EXTRA_PERSON, CastItem::class.java) ?: error("No DataExtra")
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_PERSON) ?: error("No DataExtra")
      }
    } else finish()

    binding.swipeRefresh.setOnRefreshListener {
      val i = Intent(this, PersonActivity::class.java)
      i.putExtra(EXTRA_PERSON, dataExtra)
      activityTransition()
      binding.swipeRefresh.isRefreshing = false
      startActivity(i)
      activityTransition()
      finish()
    }
  }

  private fun activityTransition() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      overrideActivityTransition(
        OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out
      )
    } else {
      @Suppress("DEPRECATION")
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
  }

  private fun showData() {
    personMovieViewModel.getLoading().observe(this) { showLoading(it) }
    personMovieViewModel.getSnackbar().observe(this) { showSnackBarWarning(it) }

    // setup recycle view and adapter
    val adapterKnownFor = KnownForAdapter()
    val adapterImage = ImagePersonAdapter { position, imageUrls ->
      showImageDialog(position, imageUrls)
    }

    binding.rvKnownFor.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.rvPhotos.adapter = adapterImage

    binding.tvName.text = dataExtra.name ?: dataExtra.originalName
    Glide.with(binding.ivPicture)
      .load(TMDB_IMG_LINK_POSTER_W500 + dataExtra.profilePath)
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_broken_image)
      .into(binding.ivPicture)

    // set button click social media
    dataExtra.id?.let { personMovieViewModel.getExternalIDPerson(it) }
    personMovieViewModel.getExternalIDPerson().observe(this) { externalID ->

      // show or hide social media
      if (!externalID.instagramId.isNullOrEmpty()) {
        binding.ivInstagram.visibility = View.VISIBLE
        binding.ivInstagram.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_LINK + externalID.instagramId))
          )
        }
      } else binding.ivInstagram.visibility = View.GONE

      if (!externalID.twitterId.isNullOrEmpty()) {
        binding.ivX.visibility = View.VISIBLE
        binding.ivX.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(X_LINK + externalID.twitterId))
          )
        }
      } else binding.ivX.visibility = View.GONE

      if (!externalID.facebookId.isNullOrEmpty()) {
        binding.ivFacebook.visibility = View.VISIBLE
        binding.ivFacebook.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_LINK + externalID.facebookId))
          )
        }
      } else binding.ivFacebook.visibility = View.GONE

      if (!externalID.tiktokId.isNullOrEmpty()) {
        binding.ivTiktok.visibility = View.VISIBLE
        binding.ivTiktok.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(TIKTOK_PERSON_LINK + externalID.tiktokId))
          )
        }
      } else binding.ivTiktok.visibility = View.GONE

      if (!externalID.youtubeId.isNullOrEmpty()) {
        binding.ivYoutube.visibility = View.VISIBLE
        binding.ivYoutube.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_CHANNEL_LINK + externalID.youtubeId))
          )
        }
      } else binding.ivYoutube.visibility = View.GONE

      if (!externalID.imdbId.isNullOrEmpty()) {
        binding.ivImdb.visibility = View.VISIBLE
        binding.ivImdb.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_PERSON_LINK + externalID.imdbId))
          )
        }
      } else binding.ivImdb.visibility = View.GONE

      if (!externalID.wikidataId.isNullOrEmpty()) {
        binding.ivWikidata.visibility = View.VISIBLE
        binding.ivWikidata.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(WIKIDATA_PERSON_LINK + externalID.wikidataId))
          )
        }
      } else binding.ivWikidata.visibility = View.GONE
    }

    // show known for
    dataExtra.id?.let { personMovieViewModel.getKnownFor(it) }
    personMovieViewModel.getKnownFor().observe(this) { adapterKnownFor.setCast(it) }

    // show picture
    dataExtra.id?.let { personMovieViewModel.getImagePerson(it) }
    personMovieViewModel.getImagePerson().observe(this) {
      adapterImage.setImage(it)
    }

    // show detail person
    dataExtra.id?.let { personMovieViewModel.getDetailPerson(it) }
    personMovieViewModel.getDetailPerson().observe(this) {
      if (it.birthday != null)
        if (it.birthday.isNotBlank() && it.birthday.isNotEmpty()) binding.tvBiography.text =
          it.biography
        else binding.tvBiography.text = getString(no_biography)
      else binding.tvBiography.text = getString(no_biography)
      showBirthdate(it)

      if (!it.homepage.isNullOrEmpty()) {
        binding.ivLink.visibility = View.VISIBLE
        binding.divider1.visibility = View.VISIBLE
        binding.ivLink.setOnClickListener { _ ->
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.homepage)))
        }
      } else {
        binding.ivLink.visibility = View.GONE
        binding.divider1.visibility = View.GONE
      }
    }

    Handler(Looper.getMainLooper()).postDelayed({
      binding.tvBiography.performClick() // set automatic click
    }, DELAY_CLICK_TIME)
  }

  private fun showImageDialog(position: Int, imageUrls: List<String>) {
    val dialog = Dialog(this)
    dialog.setContentView(dialog_image)
    dialog.window?.setDimAmount(0.8f) // set transparent percent
    dialog.window?.setLayout(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // set background transparent

    val viewPager: ViewPager2 = dialog.findViewById(view_page_dialog)
    viewPager.adapter = ImagePagerAdapter(imageUrls)
    viewPager.setCurrentItem(position, false)

    dialog.findViewById<ImageButton>(btn_close_dialog).setOnClickListener { dialog.dismiss() }
    dialog.show()
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
    }, DELAY_TIME)
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
          val birthday = "${dateFormatter(it.birthday)} (${
            getAgeBirth(it.birthday)
          } ${getString(years_old)}) \n${it.placeOfBirth}"
          binding.tvBorn.text = birthday
        } else binding.tvBorn.text = getString(no_data)
      else binding.tvBorn.text = getString(no_data)

    } else {
      binding.tvDeath.isVisible = true
      binding.tvDeadHeader.isVisible = true

      val birthDay = "${it.birthday?.let { dateFormatter(it) }} \n${it.placeOfBirth}"
      binding.tvBorn.text = birthDay
      val deathDay = "${dateFormatter(it.deathday)} (${
        it.birthday?.let { birthday -> getAgeDeath(birthday, it.deathday) }
      } ${getString(years_old)})"
      binding.tvDeath.text = deathDay
    }
  }

  private fun showSnackBarWarning(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    val snackBar = Snackbar.make(binding.constraintLayout, message, Snackbar.LENGTH_SHORT)

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(this, red_matte))
    snackBar.show()
  }

  companion object {
    const val EXTRA_PERSON = "EXTRA_PERSON"
    const val DELAY_TIME = 600L
    const val DELAY_CLICK_TIME = 800L
  }
}