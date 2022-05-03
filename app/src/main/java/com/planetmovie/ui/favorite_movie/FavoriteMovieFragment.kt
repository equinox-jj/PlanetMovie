package com.planetmovie.ui.favorite_movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteMovieBinding
import com.planetmovie.ui.adapter.ItemFavoriteMovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie) {

    // View Binding
    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    private val mFavoriteMovieViewModel: FavoriteViewModel by viewModels()
    private lateinit var mFavoriteMovieAdapter: ItemFavoriteMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteMovieBinding.bind(view)

        setHasOptionsMenu(true)
        initRecyclerView()
        initDataBinding()
    }

    private fun initRecyclerView() {
        binding.rvFavoriteMovie.apply {
            mFavoriteMovieAdapter = ItemFavoriteMovieAdapter(requireActivity(), mFavoriteMovieViewModel)
            adapter = mFavoriteMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favMovieViewModel = mFavoriteMovieViewModel
        binding.mFavMovieAdapter = mFavoriteMovieAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mFavoriteMovieAdapter.destroyContextualAction()
    }
}