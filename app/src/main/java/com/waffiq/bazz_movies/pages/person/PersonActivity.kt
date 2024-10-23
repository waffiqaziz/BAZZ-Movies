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
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.person.DetailPerson
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
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.initLinearLayoutManager
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.justifyTextView
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.utils.helpers.person.PersonPageHelper.formatBirthInfo
import com.waffiq.bazz_movies.core.utils.helpers.person.PersonPageHelper.formatDeathInfo
import com.waffiq.bazz_movies.core.utils.helpers.person.PersonPageHelper.hasAnySocialMediaIds
import com.waffiq.bazz_movies.core.utils.helpers.person.PersonPageHelper.setupSocialLink
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.ActionBarBehavior.transparentStatusBar
import com.waffiq.bazz_movies.core.utils.helpers.uihelpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core_ui.R.string.no_biography
import com.waffiq.bazz_movies.core_ui.R.string.no_data
import com.waffiq.bazz_movies.core_ui.R.string.not_available
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

    // action bar behavior
    transparentStatusBar(window)
    handleOverHeightAppBar(binding.appBarLayout)
    scrollActionBarBehavior(window, binding.appBarLayout, binding.nestedScrollViewPerson)

    justifyTextView(binding.tvBiography)
    showLoading(true)
    getDataExtra()
    setupView()
    showData()
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
  }

  private fun setupView() {
    binding.btnBack.setOnClickListener { finish() }

    binding.swipeRefresh.setOnRefreshListener {
      showData()
      binding.swipeRefresh.isRefreshing = false
    }

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

    // setup layout
    binding.rvKnownFor.layoutManager = initLinearLayoutManager(this)
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager = initLinearLayoutManager(this)
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

    showSocialMediaPerson()

    // show known for
    dataExtra.id?.let { personMovieViewModel.getKnownFor(it) }
    personMovieViewModel.knownFor.observe(this) {
      adapterKnownFor.setCast(it)
    }

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
        binding.ivLink.isVisible = true
        binding.divider1.isVisible = true
        binding.ivLink.setOnClickListener { _ ->
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(detailPerson.homepage)))
        }
      } else {
        binding.ivLink.isGone = true
        binding.divider1.isGone = true
      }
    }

    handler.postDelayed({
      binding.tvBiography.performClick() // set automatic click
    }, DELAY_CLICK_TIME)
  }

  private fun showSocialMediaPerson() {
    dataExtra.id?.let { personMovieViewModel.getExternalIDPerson(it) }
    personMovieViewModel.externalIdPerson.observe(this) { externalID ->

      if (hasAnySocialMediaIds(externalID)) {
        binding.viewGroupSocialMedia.isVisible = true
        setupSocialLink(externalID.instagramId, binding.ivInstagram, INSTAGRAM_LINK)
        setupSocialLink(externalID.twitterId, binding.ivX, X_LINK)
        setupSocialLink(externalID.facebookId, binding.ivFacebook, FACEBOOK_LINK)
        setupSocialLink(externalID.tiktokId, binding.ivTiktok, TIKTOK_PERSON_LINK)
        setupSocialLink(externalID.youtubeId, binding.ivYoutube, YOUTUBE_CHANNEL_LINK)
      } else {
        binding.viewGroupSocialMedia.isVisible = false
      }

      setupSocialLink(externalID.imdbId, binding.ivImdb, IMDB_PERSON_LINK)
      setupSocialLink(externalID.wikidataId, binding.ivWikidata, WIKIDATA_PERSON_LINK)
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

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimPerson.isVisible = true // blur background when loading
      binding.progressBar.isVisible = true
    } else {
      val animation = animFadeOutLong(this)
      binding.backgroundDimPerson.startAnimation(animation)
      binding.progressBar.startAnimation(animation)

      binding.backgroundDimPerson.isGone = true
      binding.progressBar.isGone = true
    }
  }

  private fun showBirthdate(person: DetailPerson) {
    if (person.deathday.isNullOrEmpty()) {
      binding.tvDeath.isVisible = false
      binding.tvDeadHeader.isVisible = false
    } else {
      binding.tvDeath.isVisible = true
      binding.tvDeadHeader.isVisible = true

      val deathInfo = formatDeathInfo(person.birthday, person.deathday)
      binding.tvDeath.text = deathInfo
    }

    // show birth info
    binding.tvBorn.isVisible = true
    binding.tvBornHeader.isVisible = true
    val birthInfo = formatBirthInfo(person.birthday, person.placeOfBirth)
    binding.tvBorn.text = birthInfo.ifEmpty { getString(no_data) }
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
