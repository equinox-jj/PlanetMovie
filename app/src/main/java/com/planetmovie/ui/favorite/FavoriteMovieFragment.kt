package com.planetmovie.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.planetmovie.R
import com.planetmovie.databinding.FragmentFavoriteMovieBinding

class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie) {

    // View Binding
    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteMovieBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}