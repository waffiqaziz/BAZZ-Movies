package com.waffiq.bazz_movies

import android.animation.AnimatorInflater
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
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
        bottomMargin = insets.bottom - 64
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

    binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
      if (menuItem.itemId == navigation_search) {
        animateSearchIcon(menuItem)
      }
      NavigationUI.onNavDestinationSelected(menuItem, navHostFragment.navController)
    }

    // handle search button
    binding.bottomNavigation.setOnItemReselectedListener { menuItem ->
      if (menuItem.itemId == navigation_search) {
        animateSearchIcon(menuItem)
        supportFragmentManager.setFragmentResult("open_search_view", Bundle.EMPTY)
      }
    }
  }

  private fun animateSearchIcon(item: MenuItem) {
    val menuView = binding.bottomNavigation.getChildAt(0) as ViewGroup
    val itemView = menuView.findViewById<View>(item.itemId)

    val iconView = findImageView(itemView) ?: return
    iconView.cameraDistance = iconView.resources.displayMetrics.density * 8000f
    iconView.pivotX = iconView.width / 2f
    iconView.pivotY = iconView.height / 2f

    AnimatorInflater.loadAnimator(
      iconView.context,
      R.animator.search_rotate_pop
    ).apply {
      setTarget(iconView)
      start()
    }
  }

  private fun findImageView(view: View): ImageView? {
    if (view is ImageView) return view
    if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        findImageView(view.getChildAt(i))?.let { return it }
      }
    }
    return null
  }

  override fun onDestroy() {
    super.onDestroy()
    setSupportActionBar(null)
    supportFragmentManager.fragments.forEach { fragment ->
      supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
  }
}
