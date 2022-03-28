package com.planetmovie.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.planetmovie.databinding.FragmentFavoriteBinding
import com.planetmovie.ui.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    // View Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            requireActivity()
        )

        binding.vpDetail.apply { adapter = viewPagerAdapter }

        TabLayoutMediator(binding.tlDetailFavorite, binding.vpDetail) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}