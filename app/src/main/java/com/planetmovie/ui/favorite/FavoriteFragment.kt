package com.planetmovie.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteBinding
import com.planetmovie.ui.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    // View Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = ArrayList<Fragment>()
        fragments.add(FavoriteMovieFragment())
        fragments.add(FavoriteTvFragment())

        val titles = ArrayList<String>()
        titles.add("Favorite Movie")
        titles.add("Favorite Tv")

        val bundle = Bundle()
        bundle.putString("userBundle", "boss")

        val viewPagerAdapter = ViewPagerAdapter(
            bundle,
            fragments,
            requireActivity().supportFragmentManager,
            viewLifecycleOwner.lifecycle
        )

        binding.apply {
            vpDetail.adapter = viewPagerAdapter
        }

        TabLayoutMediator(binding.tlDetailFavorite, binding.vpDetail) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}