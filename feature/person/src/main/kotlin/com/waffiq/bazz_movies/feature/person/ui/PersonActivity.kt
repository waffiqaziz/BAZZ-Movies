package com.waffiq.bazz_movies.feature.person.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.FACEBOOK_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.IMDB_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.INSTAGRAM_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TIKTOK_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W780
import com.waffiq.bazz_movies.core.common.utils.Constants.WIKIDATA_PERSON_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.X_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_CHANNEL_LINK
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_no_profile
import com.waffiq.bazz_movies.core.designsystem.R.font.nunito_sans_bold
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
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

  private lateinit var dataExtra: MediaCastItem
  private val personViewModel: PersonViewModel by viewModels()

  private val handler = Handler(Looper.getMainLooper())

  private var dialog: Dialog? = null
  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )

    super.onCreate(savedInstanceState)
    binding = ActivityPersonBinding.inflate(layoutInflater)
    setContentView(binding.root)

    addPaddingWhenNavigationEnable(binding.root)
    setSupportActionBar(binding.toolbar)

    justifyTextView(binding.tvBiography)
    typefaceTitle(binding.collapse)

    showLoading(true)

    // get the data from intent, if not available, finish the activity
    if (!getDataExtra()) return

    setupView()
    showData()
  }

  private fun typefaceTitle(collapse: CollapsingToolbarLayout) {
    collapse.setCollapsedTitleTypeface(ResourcesCompat.getFont(this, nunito_sans_bold))
    collapse.setExpandedTitleTypeface(ResourcesCompat.getFont(this, nunito_sans_bold))
  }

  private fun getDataExtra(): Boolean {
    if (!intent.hasExtra(EXTRA_PERSON)) {
      finish()
      return false
    }

    dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_PERSON, MediaCastItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_PERSON)
    } ?: error("No DataExtra")

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
    dataExtra.id?.let { personViewModel.getExternalIDPerson(it) }
    personViewModel.externalIdPerson.observe(this) { externalID ->

      if (hasAnySocialMediaIds(externalID)) {
        binding.viewGroupSocialMedia.isVisible = true
        setupSocialLink(externalID.instagramId, binding.btnInstagram, INSTAGRAM_LINK)
        setupSocialLink(externalID.twitterId, binding.btnX, X_LINK)
        setupSocialLink(externalID.facebookId, binding.btnFacebook, FACEBOOK_LINK)
        setupSocialLink(externalID.tiktokId, binding.btnTiktok, TIKTOK_PERSON_LINK)
        setupSocialLink(externalID.youtubeId, binding.btnYoutube, YOUTUBE_CHANNEL_LINK)
      } else {
        binding.viewGroupSocialMedia.isVisible = false
      }

      setupSocialLink(externalID.imdbId, binding.btnImdb, IMDB_PERSON_LINK)
      setupSocialLink(externalID.wikidataId, binding.btnWikidata, WIKIDATA_PERSON_LINK)
    }
  }

  private fun showImageDialog(position: Int, imageUrls: List<String>) {
    dialog = Dialog(this).apply {
      setContentView(dialog_image)
      window?.setDimAmount(DIALOG_ALPHA) // set transparent percent
      window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
      )
      window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable()) // set background transparent

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

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
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
