package com.waffiq.bazz_movies.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.R.id.nav_host_fragment_activity_home
import com.waffiq.bazz_movies.databinding.ActivityMainBinding
import com.waffiq.bazz_movies.ui.activity.search.SearchFragment
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val bottomNavigation: BottomNavigationView = binding.navView


    /**
     * use below if use FragmentContainerView  on activity_main.xml
     */
    val navController =
      supportFragmentManager
        .findFragmentById(nav_host_fragment_activity_home)?.findNavController()
    if (navController != null) bottomNavigation.setupWithNavController(navController)

    bottomNavigation.setOnItemReselectedListener { menuItem ->
      if (menuItem.itemId == R.id.navigation_search) {
        Log.d("MainActivity", "Search icon reselected")

        val navHostFragment = supportFragmentManager.findFragmentById(nav_host_fragment_activity_home)
        val myFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull() as? SearchFragment

        // Use WeakReference here
        myFragment?.let { fragment ->
          val weakFragment = WeakReference(fragment)

          // Now call the method on the weak reference
          weakFragment.get()?.openSearchView()
        }
      }
    }
    /**
     * user below if use fragment on activity_main.xml
     */
    // val navController = findNavController(R.id.nav_host_fragment_activity_home)
    // navView.setupWithNavController(navController)

    /**
     * Passing each menu ID as a set of Ids because each
     * menu should be considered as top level destinations.
     * setup action bar
     */
    /*
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.navigation_home, R.id.navigation_search, R.id.navigation_my_folder
      )
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    */
  }
}