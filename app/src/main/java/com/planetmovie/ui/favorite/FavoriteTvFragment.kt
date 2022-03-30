package com.planetmovie.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteTvBinding

class FavoriteTvFragment : Fragment(R.layout.fragment_favorite_tv) {

    // View Binding
    private var _binding: FragmentFavoriteTvBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteTvBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}