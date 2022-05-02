package com.planetmovie.ui.favorite_tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteTvBinding
import com.planetmovie.ui.adapter.ItemFavoriteTvAdapter
import com.planetmovie.ui.favorite_movie.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteTvFragment : Fragment(R.layout.fragment_favorite_tv) {

    // View Binding
    private var _binding: FragmentFavoriteTvBinding? = null
    private val binding get() = _binding!!

    private val mFavoriteTvViewModel: FavoriteViewModel by viewModels()
    private lateinit var mFavoriteTvAdapter: ItemFavoriteTvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteTvBinding.bind(view)

        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favTvViewModel = mFavoriteTvViewModel
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvFavoriteTv.apply {
            mFavoriteTvAdapter = ItemFavoriteTvAdapter(requireActivity(), mFavoriteTvViewModel)
            adapter = mFavoriteTvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.mFavTvAdapter = mFavoriteTvAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mFavoriteTvAdapter.destroyContextualAction()
    }
}