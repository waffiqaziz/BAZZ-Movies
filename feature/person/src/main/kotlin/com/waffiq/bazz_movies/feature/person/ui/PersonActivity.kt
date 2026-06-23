package com.waffiq.bazz_movies.feature.person.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W780
import com.waffiq.bazz_movies.core.common.utils.Constants.WIKIDATA_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.X_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_CHANNEL_LINK
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_900
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.feature.person.R.id.btn_close_dialog
import com.waffiq.bazz_movies.feature.person.R.id.view_pager_dialog
import com.waffiq.bazz_movies.feature.person.R.layout.dialog_image
import com.waffiq.bazz_movies.feature.person.databinding.ActivityPersonBinding
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.ui.adapter.ImagePagerAdapter
import com.waffiq.bazz_movies.feature.person.ui.adapter.ImagePersonAdapter
import com.waffiq.bazz_movies.feature.person.ui.adapter.KnownForAdapter
import com.waffiq.bazz_movies.feature.person.utils.helper.DialogHelper.setupTransparentDialog
import com.waffiq.bazz_movies.feature.person.utils.helper.ParcelableHelper.extractMediaCastItemFromIntent
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatDeathInfo
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.setupSocialLink
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityPersonBinding

  private lateinit var dataExtra: MediaCastItem
  private val personViewModel: PersonViewModel by viewModels()

  private val handler = Handler(Looper.getMainLooper())

  private var dialog: Dialog? = null
  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, gray_900)),
    )

    binding = ActivityPersonBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.root.setupWindowInsets()
    justifyTextView(binding.tvBiography)

    showLoading(true)

    // get the data from intent, if not available, finish the activity
    if (!extractDataFromIntent()) {
      finish()
      return
    }

    setupView()
    showData()
  }

  private fun extractDataFromIntent(): Boolean {
    val item = extractMediaCastItemFromIntent(intent) ?: return false
    dataExtra = item
    return true
  }

  private fun setupView() {
    binding.btnBack.setOnClickListener {
      onBackPressedDispatcher.onBackPressed()
    }
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
    setupRecyclerViewsWithSnap(listOf(binding.rvKnownFor, binding.rvPhotos))
    binding.rvKnownFor.adapter = adapterKnownFor
    binding.rvPhotos.adapter = adapterImage

    binding.collapse.title = dataExtra.name ?: dataExtra.originalName ?: getString(not_available)
    val imageToLoad = if (!dataExtra.profilePath.isNullOrEmpty()) {
      binding.ivPicture.contentDescription = "with_profile"
      TMDB_IMG_LINK_POSTER_W780 + dataExtra.profilePath
    } else {
      binding.ivPicture.contentDescription = "no_profile"
      ic_no_profile
    }
    Glide.with(binding.ivPicture)
      .load(imageToLoad)
      .placeholder(ic_bazz_logo)
      .transition(withCrossFade())
      .error(ic_broken_image)
      .into(binding.ivPicture)

    showSocialMediaPerson()

    // show known for
    personViewModel.castList.observe(this) {
      adapterKnownFor.setCast(it)
    }

    // show picture
    personViewModel.imageList.observe(this) {
      adapterImage.setImage(it)
    }

    // show detail person
    dataExtra.id?.let { personViewModel.getDetailPerson(it) }
    personViewModel.detailPerson.observe(this) { detailPerson ->
      binding.tvBiography.text =
        detailPerson.biography?.takeIf { it.isNotBlank() } ?: getString(no_biography)
      showBirthdate(detailPerson)

      if (!detailPerson.homepage.isNullOrEmpty()) {
        binding.btnLink.isVisible = true
        binding.divider1.isVisible = true
        binding.btnLink.setOnClickListener { _ ->
          startActivity(Intent(Intent.ACTION_VIEW, detailPerson.homepage.toUri()))
        }
      } else {
        binding.btnLink.isGone = true
        binding.divider1.isGone = true
      }
    }
  }

  private fun showSocialMediaPerson() {
    personViewModel.detailPerson.observe(this) { person ->
      val externalIds = person.externalIds

      if (externalIds == null) {
        binding.viewGroupSocialMedia.isVisible = false
        return@observe
      }

      binding.viewGroupSocialMedia.isVisible = externalIds.hasAnySocialMediaIds()

      setupSocialLink(externalIds.instagramId, binding.btnInstagram, INSTAGRAM_LINK)
      setupSocialLink(externalIds.twitterId, binding.btnX, X_LINK)
      setupSocialLink(externalIds.facebookId, binding.btnFacebook, FACEBOOK_LINK)
      setupSocialLink(externalIds.tiktokId, binding.btnTiktok, TIKTOK_PERSON_LINK)
      setupSocialLink(externalIds.youtubeId, binding.btnYoutube, YOUTUBE_CHANNEL_LINK)

      setupSocialLink(externalIds.imdbId, binding.btnImdb, IMDB_PERSON_LINK)
      setupSocialLink(externalIds.wikidataId, binding.btnWikidata, WIKIDATA_PERSON_LINK)
    }
  }

  private fun showImageDialog(position: Int, imageUrls: List<String>) {
    dialog = Dialog(this).apply {
      setContentView(dialog_image)
      setupTransparentDialog() // set background transparent

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
      binding.nestedScrollViewPerson.isNestedScrollingEnabled = false
      binding.swipeRefresh.isEnabled = false
    } else {
      val animation = animFadeOutLong(this)
      binding.backgroundDimPerson.startAnimation(animation)
      binding.progressBar.startAnimation(animation)

      binding.nestedScrollViewPerson.isNestedScrollingEnabled = true
      binding.swipeRefresh.isEnabled = true
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
    val birthInfo = formatBirthInfo(person.birthday, person.placeOfBirth, person.deathday)
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
  }
}
