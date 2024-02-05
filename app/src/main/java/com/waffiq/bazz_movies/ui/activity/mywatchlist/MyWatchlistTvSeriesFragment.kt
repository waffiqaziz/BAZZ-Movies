package com.waffiq.bazz_movies.ui.activity.mywatchlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.databinding.FragmentMyWatchlistTvSeriesBinding
import com.waffiq.bazz_movies.ui.adapter.FavoriteAdapterDB
import com.waffiq.bazz_movies.ui.adapter.FavoriteTvAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class MyWatchlistTvSeriesFragment : Fragment() {
  private var _binding: FragmentMyWatchlistTvSeriesBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: MyWatchlistViewModel
  private lateinit var viewModelAuth: AuthenticationViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyWatchlistTvSeriesBinding.inflate(inflater, container, false)
    val root = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    viewModel = ViewModelProvider(this, factory)[MyWatchlistViewModel::class.java]

    val pref = requireContext().dataStore
    val factoryAuth = ViewModelUserFactory.getInstance(pref)
    this.viewModelAuth = ViewModelProvider(this, factoryAuth)[AuthenticationViewModel::class.java]

    checkUser()
    return root
  }

  private fun checkUser() {
    //setup recycleview
    binding.rvWatchlistTv.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    viewModelAuth.getUser().observe(viewLifecycleOwner) { user ->
      if (user.token != "NaN") { //user login then show favorite data from TMDB API
        setDataUserLogin(user.token)
      } else { //guest user then show favorite data from database
        setDataGuestUser()
        binding.rvWatchlistTv.addItemDecoration(
          DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
      }
    }
  }

  private fun setDataUserLogin(userToken: String) {
    val adapterPaging = FavoriteTvAdapter()

    binding.rvWatchlistTv.adapter = adapterPaging.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterPaging.retry()
      }
    )

    //show or hide view
    adapterPaging.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapterPaging.itemCount < 1
      ) {
        /// show empty view
        binding.viewEmpty.isVisible = true
      } else {
        ///  hide empty view
      }

      binding.progressBar.isVisible =
        loadState.source.refresh is LoadState.Loading //show progressbar
    }

    viewModel.getWatchlistTvSeries(userToken)
      .observe(viewLifecycleOwner) {
        adapterPaging.submitData(lifecycle, it)
      }
  }

  private fun setDataGuestUser() {
    val adapterDB = FavoriteAdapterDB()
    binding.rvWatchlistTv.adapter = adapterDB

    viewModel.getWatchlistTvSeriesDB.observe(viewLifecycleOwner) {
      adapterDB.setFavorite(it)
      binding.viewEmpty.visibility = if (it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
//      binding.progressBar.visibility = if (it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}