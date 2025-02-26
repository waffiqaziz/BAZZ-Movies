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
import com.waffiq.bazz_movies.feature.search.ui.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

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

    // handle behavior Android 15 changes
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

    // Get NavHostFragment
    val navHostFragment =
      supportFragmentManager.findFragmentById(nav_host_fragment_activity_home) as NavHostFragment

    // Setup the BottomNavigationView with NavController
    binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

    // Expand SearchView on FragmentSearch when navigation search clicked twice
    binding.bottomNavigation.setOnItemReselectedListener { menuItem ->
      if (menuItem.itemId == navigation_search) {
        val searchFragment = navHostFragment.childFragmentManager.fragments
          .firstOrNull { it is SearchFragment } as? SearchFragment

        searchFragment?.let { fragment ->
          WeakReference(fragment).get()?.openSearchView()
        }
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
