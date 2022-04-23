package com.planetmovie.ui.favorite_movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteMovieBinding
import com.planetmovie.ui.adapter.ItemFavoriteMovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie) {

    // View Binding
    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    private val mFavoriteMovieAdapter: ItemFavoriteMovieAdapter by lazy { ItemFavoriteMovieAdapter(requireActivity(),  mFavoriteMovieViewModel) }
    private val mFavoriteMovieViewModel: FavoriteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteMovieBinding.bind(view)

        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favMovieViewModel = mFavoriteMovieViewModel
        binding.mFavMovieAdapter = mFavoriteMovieAdapter
        initRecyclerView(binding.rvFavoriteMovie)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mFavoriteMovieAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mFavoriteMovieAdapter.destroyContextualAction()
    }
}