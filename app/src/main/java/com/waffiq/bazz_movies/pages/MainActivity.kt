package com.waffiq.bazz_movies.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.waffiq.bazz_movies.R.id.nav_host_fragment_activity_home
import com.waffiq.bazz_movies.R.id.navigation_search
import com.waffiq.bazz_movies.databinding.ActivityMainBinding
import com.waffiq.bazz_movies.pages.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Get NavHostFragment
    val navHostFragment =
      supportFragmentManager.findFragmentById(nav_host_fragment_activity_home) as NavHostFragment

    // Setup the BottomNavigationView with NavController
    binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

    // Expand SearchView on FragmentSearch when navigation search clicked twice
    binding.bottomNavigation.setOnItemReselectedListener { menuItem ->
      if (menuItem.itemId == navigation_search) {
        val myFragment =
          navHostFragment.childFragmentManager.fragments.firstOrNull() as? SearchFragment // search fragment

        myFragment?.let { fragment ->
          WeakReference(fragment).get()?.openSearchView()
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    setSupportActionBar(null)
  }
}
