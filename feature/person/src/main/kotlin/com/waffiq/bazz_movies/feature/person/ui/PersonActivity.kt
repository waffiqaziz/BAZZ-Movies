package com.waffiq.bazz_movies.feature.person.ui

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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.FACEBOOK_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.IMDB_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.INSTAGRAM_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TIKTOK_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.common.utils.Constants.WIKIDATA_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.X_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_CHANNEL_LINK
import com.waffiq.bazz_movies.core.data.MovieTvCastItem
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.movie.utils.helpers.GeneralHelper.initLinearLayoutManagerHorizontal
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.transparentStatusBar
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.feature.person.R.id.btn_close_dialog
import com.waffiq.bazz_movies.feature.person.R.id.view_pager_dialog
import com.waffiq.bazz_movies.feature.person.R.layout.dialog_image
import com.waffiq.bazz_movies.feature.person.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.ui.adapter.ImagePagerAdapter
import com.waffiq.bazz_movies.feature.person.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.feature.person.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatDeathInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.hasAnySocialMediaIds
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.setupSocialLink
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityPersonBinding

  private lateinit var dataExtra: MovieTvCastItem
  private val personViewModel: PersonViewModel by viewModels()

  private val handler = Handler(Looper.getMainLooper())

  private var dialog: Dialog? = null
  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPersonBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // action bar behavior
    window.transparentStatusBar()
    binding.appBarLayout.handleOverHeightAppBar()
    scrollActionBarBehavior(window, binding.appBarLayout, binding.nestedScrollViewPerson)
    addPaddingWhenNavigationEnable(binding.root)

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
    personViewModel.errorState.observe(this) {
      mSnackbar = snackBarWarning(binding.coordinatorLayout, null, it)
    }
    personViewModel.loadingState.observe(this) { showLoading(it) }
  }

  private fun showData() {
    // setup recycle view and adapter
    val adapterKnownFor = KnownForAdapter(navigator)
    val adapterImage = ImagePersonAdapter { position, imageUrls ->
      showImageDialog(position, imageUrls)
    }

    // setup layout
    binding.rvKnownFor.layoutManager = initLinearLayoutManagerHorizontal(this)
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.layoutManager = initLinearLayoutManagerHorizontal(this)
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
    dataExtra.id?.let { personViewModel.getKnownFor(it) }
    personViewModel.knownFor.observe(this) {
      adapterKnownFor.setCast(it)
    }

    // show picture
    dataExtra.id?.let { personViewModel.getImagePerson(it) }
    personViewModel.imagePerson.observe(this) {
      adapterImage.setImage(it)
    }

    // show detail person
    dataExtra.id?.let { personViewModel.getDetailPerson(it) }
    personViewModel.detailPerson.observe(this) { detailPerson ->
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
    dataExtra.id?.let { personViewModel.getExternalIDPerson(it) }
    personViewModel.externalIdPerson.observe(this) { externalID ->

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
      window?.setDimAmount(DIALOG_ALPHA) // set transparent percent
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
    const val DIALOG_ALPHA = 0.8f
    const val EXTRA_PERSON = "EXTRA_PERSON"
    const val DELAY_CLICK_TIME = 800L
  }
}
