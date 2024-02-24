package com.waffiq.bazz_movies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.ActivityMainBinding

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
        .findFragmentById(R.id.nav_host_fragment_activity_home)?.findNavController()
    if (navController != null) bottomNavigation.setupWithNavController(navController)

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

    // custom action bar
    val actionBarTitleView = layoutInflater.inflate(R.layout.custom_action_bar_title, null)
    supportActionBar?.apply {
      setDisplayShowCustomEnabled(true)
      setDisplayShowHomeEnabled(true)
      setIcon(R.mipmap.ic_launcher)
      customView = actionBarTitleView
      title = null // Hide the default title
    }
  }
}