package com.waffiq.bazz_movies.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waffiq.bazz_movies.ui.activity.home.FeaturedFragment
import com.waffiq.bazz_movies.ui.activity.home.MovieFragment
import com.waffiq.bazz_movies.ui.activity.home.TvSeriesFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeaturedFragment()
            1 -> MovieFragment()
            else -> TvSeriesFragment()
        }

    }

    companion object {
        const val NUM_TABS = 3
    }
}