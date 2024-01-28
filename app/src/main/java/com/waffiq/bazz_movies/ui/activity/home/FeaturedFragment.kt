package com.waffiq.bazz_movies.ui.activity.home

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.FragmentFeaturedBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieHomeAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.Helper.getLocation
import com.waffiq.bazz_movies.utils.Helper.showToastShort
import java.util.concurrent.TimeUnit


class FeaturedFragment : Fragment() {

  private var _binding: FragmentFeaturedBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: HomeViewModel

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var locationRequest: LocationRequest
  private lateinit var locationCallback: LocationCallback

  private lateinit var region: String

  @RequiresApi(Build.VERSION_CODES.S)
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    getMyLastLocation()
    createLocationRequest()
//    createLocationCallback()

    setMoveNowPlaying()
    setData()
    return root
  }

  private fun setMoveNowPlaying() {
//    val data = viewModel.getFirstMovieNowPlaying()
//    viewModel.getFirstMovieNowPlaying().observe(viewLifecycleOwner) {
//      Log.e("Cek Link : ", it[0].backdropPath)
//    }

    // show main picture
    Glide.with(binding.imgMainFeatured)
      //.load("http://image.tmdb.org/t/p/w500/" + data.backdropPath) // URL movie poster
      .load(TMDB_IMG_LINK_BACKDROP_W780 + "bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg") // URL movie poster
      .placeholder(R.mipmap.ic_launcher)
      .error(R.drawable.ic_broken_image)
      .into(binding.imgMainFeatured)
  }

  // get the location data
  private fun createLocationCallback() {
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
          Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
        }
      }
    }
  }

  private val locationPermissionRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val granted = permissions.entries.all { it.value }

    if (granted) {
      getMyLastLocation()
    } else showToastShort(requireContext(), "Permission Denied")
  }

  // show prompt to activated gps
  private val resolutionLauncher =
    registerForActivityResult(
      ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
      when (result.resultCode) {
        RESULT_OK ->
          Log.i(TAG, "All location settings are satisfied.")

        // this will show if user didn't activate gps
        RESULT_CANCELED ->
          showToastShort(
            requireContext(),
            getString(R.string.please_enable_gps),
          )
      }
    }

  private fun createLocationRequest() {
    locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1).apply {
      setMinUpdateDistanceMeters(1F)
      setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
      setWaitForAccurateLocation(true)
    }.build()

    // check if gps active/not
    val builder = LocationSettingsRequest.Builder()
      .addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(requireContext())
    client.checkLocationSettings(builder.build())
      .addOnSuccessListener {
        getMyLastLocation()
      }
      .addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
          try {
            resolutionLauncher.launch(
              IntentSenderRequest.Builder(exception.resolution).build()
            )
          } catch (sendEx: IntentSender.SendIntentException) {
            sendEx.message?.let { showToastShort(requireContext(), it) }
          }
        }
      }
  }

  private fun checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
      requireContext(),
      permission
    ) == PackageManager.PERMISSION_GRANTED
  }

  private fun getMyLastLocation() {
    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
      checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    ) {
      fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) Log.e("KKK", "Lat: ${location.latitude}, Long: ${location.longitude}")
        else showToastShort(requireContext(), "Location is not found. Try Again")
      }
    } else locationPermissionRequest.launch(PERMISSIONS)
  }

  private fun setData() {
    // get region
    region = getLocation(requireContext())
    if (region.isNotEmpty()) {

      // check permission
      if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
      ){
        fusedLocationClient.getCurrentLocation(
          Priority.PRIORITY_LOW_POWER,
          object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
              CancellationTokenSource().token

            override fun isCancellationRequested() = false
          })
          .addOnSuccessListener { location: Location? ->
            if (location == null)
              showToastShort(requireContext(), "Cannot get location.")
            else {
              val lat = location.latitude
              val lon = location.longitude

              Log.e("KKKK", "$lat $lon")
            }
          }
      }
    }

    binding.rvTrending.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.rvUpcoming.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.rvPlayingNow.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapterTrending = TrendingAdapter()
    val adapterUpcoming = MovieHomeAdapter()
    val adapterPlayingNow = MovieHomeAdapter()

    // trending
    binding.rvTrending.adapter = adapterTrending.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterTrending.retry() }
    )
    viewModel.getTrending(region).observe(viewLifecycleOwner) {
      adapterTrending.submitData(lifecycle, it)
    }

    // upcoming movie
    binding.rvUpcoming.adapter = adapterUpcoming.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterUpcoming.retry() }
    )
    viewModel.getUpcomingMovies(region).observe(viewLifecycleOwner) {
      adapterUpcoming.submitData(lifecycle, it)
    }

    // playing not at theater
    binding.rvPlayingNow.adapter = adapterPlayingNow.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterPlayingNow.retry() }
    )
    viewModel.getPlayingNowMovies(region).observe(viewLifecycleOwner) {
      adapterPlayingNow.submitData(lifecycle, it)
    }
  }

  private fun animFadeOut() {
    val animation = Helper.animFadeOutLong(requireContext())
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.progressBar.visibility = View.GONE
    }, 600)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.visibility = View.VISIBLE
    } else animFadeOut()
  }

  private fun startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED &&
      ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      return
    }
    fusedLocationClient.requestLocationUpdates(
      locationRequest,
      locationCallback,
      null /* Looper */
    )
  }

  private fun stopLocationUpdates() {
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

//  override fun onPause() {
//    super.onPause()
//    stopLocationUpdates()
//  }
//
//  override fun onResume() {
//    super.onResume()
//    startLocationUpdates()
//  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    private const val TAG = "FeatureFragment"

    var PERMISSIONS = arrayOf(
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION
    )
  }

}