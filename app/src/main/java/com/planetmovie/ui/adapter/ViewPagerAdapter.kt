package com.planetmovie.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private val bundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    fragmentsActivity: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentsActivity, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = bundle
        return fragments[position]
    }

}