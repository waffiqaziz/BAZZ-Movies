package com.waffiq.bazz_movies

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.waffiq.bazz_movies.R.id.nav_host_fragment_activity_home
import com.waffiq.bazz_movies.R.id.navigation_search
import com.waffiq.bazz_movies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
    )

    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupWindowInsets()
    setupNavigation()
  }

  private fun setupWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
      val insets = windowInsets.getInsets(
        WindowInsetsCompat.Type.systemBars()
          or WindowInsetsCompat.Type.displayCutout()
      )
      v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = insets.top
        leftMargin = insets.left
        bottomMargin = insets.bottom
        rightMargin = insets.right
      }
      WindowInsetsCompat.CONSUMED
    }
  }

  private fun setupNavigation() {
    // get NavHostFragment
    val navHostFragment =
      supportFragmentManager.findFragmentById(nav_host_fragment_activity_home) as NavHostFragment

    // setup the BottomNavigationView with NavController
    binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

    // handle search button
    binding.bottomNavigation.setOnItemReselectedListener { menuItem ->
      if (menuItem.itemId == navigation_search) {
        supportFragmentManager.setFragmentResult("open_search_view", Bundle.EMPTY)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    setSupportActionBar(null)
    supportFragmentManager.fragments.forEach { fragment ->
      supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
  }
}
