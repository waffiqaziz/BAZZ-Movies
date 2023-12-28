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

    val navView: BottomNavigationView = binding.navView

    val navController =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
        ?.findNavController()

    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.

//    // Setup Action Bar
//    val appBarConfiguration = AppBarConfiguration(
//      setOf(
//        R.id.navigation_home, R.id.navigation_search, R.id.navigation_my_folder
//      )
//    )
//    setupActionBarWithNavController(navController, appBarConfiguration)

    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setIcon(R.mipmap.ic_launcher)
    if (navController != null) {
      navView.setupWithNavController(navController)
    }
  }
}