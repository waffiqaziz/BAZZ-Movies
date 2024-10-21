package com.waffiq.bazz_movies.pages.person

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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.anim.fade_in
import com.waffiq.bazz_movies.R.anim.fade_out
import com.waffiq.bazz_movies.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.drawable.ic_no_profile
import com.waffiq.bazz_movies.R.id.btn_close_dialog
import com.waffiq.bazz_movies.R.id.view_pager_dialog
import com.waffiq.bazz_movies.R.layout.dialog_image
import com.waffiq.bazz_movies.core_ui.R.string.no_biography
import com.waffiq.bazz_movies.core_ui.R.string.no_data
import com.waffiq.bazz_movies.core_ui.R.string.not_available
import com.waffiq.bazz_movies.core_ui.R.string.years_old
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.person.DetailPerson
import com.waffiq.bazz_movies.core.domain.model.person.ExternalIDPerson
import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCastItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.adapter.ImagePagerAdapter
import com.waffiq.bazz_movies.core.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.core.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.core.utils.common.Constants.FACEBOOK_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.IMDB_PERSON_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.INSTAGRAM_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.TIKTOK_PERSON_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.utils.common.Constants.WIKIDATA_PERSON_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.X_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.YOUTUBE_CHANNEL_LINK
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.animFadeOutLong
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.justifyTextView
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.transparentStatusBar
import com.waffiq.bazz_movies.core.utils.helpers.PersonPageHelper.getAgeBirth
import com.waffiq.bazz_movies.core.utils.helpers.PersonPageHelper.getAgeDeath
import com.waffiq.bazz_movies.core.utils.helpers.PersonPageHelper.hasAnySocialMediaIds
import com.waffiq.bazz_movies.core.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.pages.detail.DetailMovieActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonActivity : AppCompatActivity(), DetailNavigator {

  private lateinit var binding: ActivityPersonBinding
  private lateinit var dataExtra: MovieTvCastItem
  private val personMovieViewModel: PersonMovieViewModel by viewModels()

  private var dialog: Dialog? = null
  private val handler = Handler(Looper.getMainLooper())

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPersonBinding.inflate(layoutInflater)
    setContentView(binding.root)

    justifyTextView(binding.tvBiography)
    transparentStatusBar(window)
    scrollActionBarBehavior(binding.appBarLayout, binding.nestedScrollViewPerson)
    showLoading(true)
    getDataExtra()
    setupView()
    showData()
    btnListener()
  }

  private fun getDataExtra() {
    // check if intent hasExtra for early return
    if (!intent.hasExtra(EXTRA_PERSON)) {
      finish()
      return
    }

    dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_PERSON, MovieTvCastItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_PERSON)
    } ?: error("No DataExtra")

    binding.swipeRefresh.setOnRefreshListener {
      showData()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupView() {
    // error and loading handle
    personMovieViewModel.errorState.observe(this) {
      mSnackbar = snackBarWarning(binding.coordinatorLayout, null, it)
    }
    personMovieViewModel.loadingState.observe(this) { showLoading(it) }
  }

  private fun showData() {
    // setup recycle view and adapter
    val adapterKnownFor = KnownForAdapter(this)
    val adapterImage = ImagePersonAdapter { position, imageUrls ->
      showImageDialog(position, imageUrls)
    }

    binding.rvKnownFor.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.rvPhotos.adapter = adapterImage

    binding.tvName.text = dataExtra.name ?: dataExtra.originalName ?: getString(not_available)
    Glide.with(binding.ivPicture)
      .load(
        if (!dataExtra.profilePath.isNullOrEmpty()) {
          TMDB_IMG_LINK_POSTER_W500 + dataExtra.profilePath
        } else {
          ic_no_profile
        }
      )
      .placeholder(ic_bazz_logo)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(binding.ivPicture)

    // social media
    dataExtra.id?.let { personMovieViewModel.getExternalIDPerson(it) }
    personMovieViewModel.externalIdPerson.observe(this) { externalID ->

      if (hasAnySocialMediaIds(externalID)) {
        showSocialMediaPerson(externalID)
      } else {
        binding.viewGroupSocialMedia.visibility = View.GONE
      }

      if (!externalID.imdbId.isNullOrEmpty()) {
        binding.ivImdb.visibility = View.VISIBLE
        binding.ivImdb.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_PERSON_LINK + externalID.imdbId))
          )
        }
      } else {
        binding.ivImdb.visibility = View.GONE
      }

      if (!externalID.wikidataId.isNullOrEmpty()) {
        binding.ivWikidata.visibility = View.VISIBLE
        binding.ivWikidata.setOnClickListener {
          startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(WIKIDATA_PERSON_LINK + externalID.wikidataId))
          )
        }
      } else {
        binding.ivWikidata.visibility = View.GONE
      }
    }

    // show known for
    dataExtra.id?.let { personMovieViewModel.getKnownFor(it) }
    personMovieViewModel.knownFor.observe(this) { adapterKnownFor.setCast(it) }

    // show picture
    dataExtra.id?.let { personMovieViewModel.getImagePerson(it) }
    personMovieViewModel.imagePerson.observe(this) {
      adapterImage.setImage(it)
    }

    // show detail person
    dataExtra.id?.let { personMovieViewModel.getDetailPerson(it) }
    personMovieViewModel.detailPerson.observe(this) { detailPerson ->
      binding.tvBiography.text =
        detailPerson.biography?.takeIf { it.isNotBlank() } ?: getString(no_biography)
      showBirthdate(detailPerson)

      if (!detailPerson.homepage.isNullOrEmpty()) {
        binding.ivLink.visibility = View.VISIBLE
        binding.divider1.visibility = View.VISIBLE
        binding.ivLink.setOnClickListener { _ ->
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(detailPerson.homepage)))
        }
      } else {
        binding.ivLink.visibility = View.GONE
        binding.divider1.visibility = View.GONE
      }
    }

    handler.postDelayed({
      binding.tvBiography.performClick() // set automatic click
    }, DELAY_CLICK_TIME)
  }

  private fun showSocialMediaPerson(externalID: ExternalIDPerson) {
    binding.viewGroupSocialMedia.visibility = View.VISIBLE
    if (!externalID.instagramId.isNullOrEmpty()) {
      binding.ivInstagram.visibility = View.VISIBLE
      binding.ivInstagram.setOnClickListener {
        startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_LINK + externalID.instagramId))
        )
      }
    } else {
      binding.ivInstagram.visibility = View.GONE
    }

    if (!externalID.twitterId.isNullOrEmpty()) {
      binding.ivX.visibility = View.VISIBLE
      binding.ivX.setOnClickListener {
        startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(X_LINK + externalID.twitterId))
        )
      }
    } else {
      binding.ivX.visibility = View.GONE
    }

    if (!externalID.facebookId.isNullOrEmpty()) {
      binding.ivFacebook.visibility = View.VISIBLE
      binding.ivFacebook.setOnClickListener {
        startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_LINK + externalID.facebookId))
        )
      }
    } else {
      binding.ivFacebook.visibility = View.GONE
    }

    if (!externalID.tiktokId.isNullOrEmpty()) {
      binding.ivTiktok.visibility = View.VISIBLE
      binding.ivTiktok.setOnClickListener {
        startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(TIKTOK_PERSON_LINK + externalID.tiktokId))
        )
      }
    } else {
      binding.ivTiktok.visibility = View.GONE
    }

    if (!externalID.youtubeId.isNullOrEmpty()) {
      binding.ivYoutube.visibility = View.VISIBLE
      binding.ivYoutube.setOnClickListener {
        startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_CHANNEL_LINK + externalID.youtubeId))
        )
      }
    } else {
      binding.ivYoutube.visibility = View.GONE
    }
  }

  private fun showImageDialog(position: Int, imageUrls: List<String>) {
    dialog = Dialog(this).apply {
      setContentView(dialog_image)
      window?.setDimAmount(0.8f) // set transparent percent
      window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
      )
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // set background transparent

      val viewPager: ViewPager2 = findViewById(view_pager_dialog)
      viewPager.adapter = ImagePagerAdapter(imageUrls)
      viewPager.setCurrentItem(position, false)

      findViewById<ImageButton>(btn_close_dialog).setOnClickListener { dismiss() }
      show()
    }
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }
  }

  private fun animFadeOut() {
    val animation = animFadeOutLong(this)
    binding.backgroundDimPerson.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    binding.backgroundDimPerson.visibility = View.GONE
    binding.progressBar.visibility = View.GONE
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimPerson.visibility = View.VISIBLE // blur background when loading
      binding.progressBar.visibility = View.VISIBLE
    } else {
      animFadeOut()
    }
  }

  private fun showBirthdate(it: DetailPerson) {
    if (it.deathday.isNullOrEmpty()) {
      binding.tvDeath.isVisible = false
      binding.tvDeadHeader.isVisible = false

      it.birthday?.let { birthday ->
        if (birthday.isNotBlank()) {
          val formattedBirthday =
            "${dateFormatterStandard(birthday)} (${getAgeBirth(birthday)} ${getString(years_old)}) \n${it.placeOfBirth}"
          binding.tvBorn.text = formattedBirthday
        } else {
          binding.tvBorn.text = getString(no_data)
        }
      } ?: run {
        binding.tvBorn.text = getString(no_data)
      }

    } else {
      binding.tvDeath.isVisible = true
      binding.tvDeadHeader.isVisible = true

      val birthDay = "${it.birthday?.let { dateFormatterStandard(it) }} \n${it.placeOfBirth}"
      binding.tvBorn.text = birthDay
      it.deathday?.let { deathday ->
        val formattedDeathDay = "${dateFormatterStandard(deathday)} (${
          it.birthday?.let { birthday -> getAgeDeath(birthday, deathday) }
        } ${getString(years_old)})"
        binding.tvDeath.text = formattedDeathDay
      } ?: run {
        binding.tvDeath.text = getString(no_data)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    mSnackbar?.dismiss()
    mSnackbar = null
    handler.removeCallbacksAndMessages(null)
    dialog?.dismiss()
    dialog = null
    Glide.get(this).clearMemory()
  }

  companion object {
    const val EXTRA_PERSON = "EXTRA_PERSON"
    const val DELAY_CLICK_TIME = 800L
  }

  override fun openDetails(resultItem: ResultItem) {
    val intent = Intent(this, DetailMovieActivity::class.java)
    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
    val options =
      ActivityOptionsCompat.makeCustomAnimation(this, fade_in, fade_out)
    ActivityCompat.startActivity(this, intent, options.toBundle())
  }
}