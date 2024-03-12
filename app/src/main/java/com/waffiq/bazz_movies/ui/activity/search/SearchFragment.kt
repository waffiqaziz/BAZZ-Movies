package com.waffiq.bazz_movies.ui.activity.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.R.string.clear_query
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.R.menu.search_menu
import com.waffiq.bazz_movies.R.id.action_search
import com.waffiq.bazz_movies.R.drawable.ic_search
import com.waffiq.bazz_movies.R.drawable.ic_cross
import com.waffiq.bazz_movies.databinding.FragmentSearchBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.SearchAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.showToastShort

class SearchFragment : Fragment(), BottomSheetSearchFragment.ListenerFilter {

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private lateinit var searchViewModel: SearchViewModel
  private lateinit var closeButton: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

    (activity as AppCompatActivity).supportActionBar?.show()

    setupSearchView(searchViewModel)
    setupFab()
    setupFilter()
    return root
  }

  private fun setupSearchView(searchViewModel: SearchViewModel) {
    // setup recycleView
    binding.rvSearch.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    val adapter = SearchAdapter()

    binding.rvSearch.adapter = adapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapter.retry()
      }
    )

    // show or hide view
    adapter.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading
        && loadState.append.endOfPaginationReached
        && adapter.itemCount < 1
      ) {
        /// show empty view
        binding.illustrationSearchNoResultView.containerSearchNoResult.isVisible = true
        binding.illustrationSearchView.containerSearch.isVisible = false
      } else {
        ///  hide empty view
        binding.illustrationSearchView.containerSearch.isVisible = false
        binding.illustrationSearchNoResultView.containerSearchNoResult.isVisible = false
      }
    }

    //setup searchView
    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(search_menu, menu)

        val searchView = menu.findItem(action_search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        customizeSearchView(searchView)

        // search queryTextChange ListenerFilter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

          override fun onQueryTextSubmit(query: String?): Boolean { //when user submit data

            if (query != null) {
              searchViewModel.search(query).observe(viewLifecycleOwner) { resultItemSearch ->
                adapter.submitData(lifecycle, resultItemSearch)
                adapter.addLoadStateListener {
                  binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
                }
              }
            }
            // hide virtual keyboard when submitted
            searchView.clearFocus()

            // show fab
            binding.fabFilter.visibility = View.VISIBLE

            return true
          }

          override fun onQueryTextChange(query: String?): Boolean {
            return true
          }
        })
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          action_search -> true
          else -> false
        }
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun customizeSearchView(searchView: SearchView) {
    lateinit var backButton: ImageView
    val searchPlate = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
    traverseViewHierarchy(searchPlate)

    for (i in 0 until searchView.childCount) {
      val child = searchView.getChildAt(i)
      if (child is ImageView) {
        // Check if the child is the arrow icon
        backButton = child
        // Set your custom drawable
        val drawable: Drawable? = ContextCompat.getDrawable(requireActivity(), ic_search)
        backButton.setImageDrawable(drawable)
        break
      }
    }
  }

  private fun traverseViewHierarchy(view: View) {
    if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        val child = view.getChildAt(i)
        if (child is ViewGroup) {
          traverseViewHierarchy(child)
        } else if (child is View && child.contentDescription == "Clear query") {
          closeButton = child
          val ivCloseButton = closeButton as ImageView
          ivCloseButton.contentDescription = getString(clear_query)
          ivCloseButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(), ic_cross))
        }
      }
    }
  }

  private fun setupFilter() {

    // show filter via DialogFragment
    binding.fabFilter.setOnClickListener {
      val bottomSheetDialogFragment = BottomSheetSearchFragment(this)
      bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
    }
  }

  private fun setupFab() {
    binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        // if the recycler view is scroll down hide the FAB
        if (dy > 5 && binding.fabFilter.isShown) binding.fabFilter.hide()

        // if the recycler view is scroll up show the FAB
        if (dy < -5 && !binding.fabFilter.isShown) binding.fabFilter.show()

        // recycler view at the first time fab always shown
        if (!recyclerView.canScrollVertically(-1)) binding.fabFilter.show()
      }
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun passData(data: Any) {
    showToastShort(requireActivity(), data.toString())
  }
}