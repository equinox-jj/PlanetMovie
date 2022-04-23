package com.planetmovie.ui.favorite_tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val mFavoriteTvAdapter: ItemFavoriteTvAdapter by lazy { ItemFavoriteTvAdapter(requireActivity(), mFavoriteTvViewModel) }
    private val mFavoriteTvViewModel: FavoriteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteTvBinding.bind(view)

        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favTvViewModel = mFavoriteTvViewModel
        binding.mFavTvAdapter = mFavoriteTvAdapter
        initRecyclerView(binding.rvFavoriteTv)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mFavoriteTvAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mFavoriteTvAdapter.destroyContextualAction()
    }
}