package com.planetmovie.ui.moviedetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.databinding.FragmentMovieDetailBinding
import com.planetmovie.ui.adapter.ItemCastAdapter
import com.planetmovie.ui.adapter.ItemTrailerAdapter
import com.planetmovie.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    // View Binding
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mMovieDetailViewModel: MovieDetailViewModel by viewModels()

    // Adapter
    private val mMovieDetailCastAdapter: ItemCastAdapter by lazy { ItemCastAdapter() }
    private val mMovieDetailTrailerAdapter: ItemTrailerAdapter by lazy { ItemTrailerAdapter() }

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    // Navigation Args
    private val args by navArgs<MovieDetailFragmentArgs>()

    // Favorite Menu
    private lateinit var favMenuItem: MenuItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)
        setupRecycler()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.rvDtlCast.apply {
            adapter = mMovieDetailCastAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvDtlTrailer.apply {
            binding.rvDtlTrailer.adapter = mMovieDetailTrailerAdapter
            binding.rvDtlTrailer.setHasFixedSize(true)
            binding.rvDtlTrailer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModel() {
        val movieId: Int = args.movieId
        mMovieDetailViewModel.getMovieDetail(movieId)
        mMovieDetailViewModel.movieDetail.observe(viewLifecycleOwner) { detailMovie ->
            when (detailMovie) {
                is Resource.Success -> {
                    showShimmer(false)
                    detailMovie.data?.let { detailData(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun detailData(data: MovieDetailResponse) {
        data.let {
            binding.apply {
                val rating = "${it.voteAverage} / 10"
                rbDtlOne.rating = it.voteAverage.div(2).toFloat()
                tvDtlDate.text = it.releaseDate
                tvDtlRate.text = rating
                ivDtlMovieBackdrop.load(Constant.BASE_IMG_URL_BACKDROP + it.backdropPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                ivDtlPoster.load(Constant.BASE_IMG_URL_POSTER + it.posterPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                if (it.overview.isNotEmpty()) {
                    tvDtlOverview.text = it.overview
                } else tvDtlOverview.text = getString(R.string.overview_not_available)
                if (it.movieCredits.movieCast.isNotEmpty()) {
                    mMovieDetailCastAdapter.castDiffUtil(it.movieCredits)
                } else tvCast.visibility = View.GONE
                if (it.movieVideos.movieVideoResults.isNotEmpty()) {
                    mMovieDetailTrailerAdapter.trailerDiffUtil(it.movieVideos)
                } else tvTrailer.visibility = View.GONE
            }
        }
    }

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerDetailMovie.startShimmer()
                shimmerDetailMovie.visibility = View.VISIBLE
                ivDtlMovieBackdrop.visibility = View.GONE
                shadowBackdrop.visibility = View.GONE
                ivDtlPoster.visibility = View.GONE
                tvRating.visibility = View.GONE
                tvDtlRate.visibility = View.GONE
                rbDtlOne.visibility = View.GONE
                tvReleaseDate.visibility = View.GONE
                tvDtlDate.visibility = View.GONE
                tvOverview.visibility = View.GONE
                tvDtlOverview.visibility = View.GONE
                tvCast.visibility = View.GONE
                rvDtlCast.visibility = View.GONE
                tvTrailer.visibility = View.GONE
                rvDtlTrailer.visibility = View.GONE
            } else {
                isShimmerLoading = false
                shimmerDetailMovie.stopShimmer()
                shimmerDetailMovie.visibility = View.GONE
                ivDtlMovieBackdrop.visibility = View.VISIBLE
                shadowBackdrop.visibility = View.VISIBLE
                ivDtlPoster.visibility = View.VISIBLE
                tvRating.visibility = View.VISIBLE
                tvDtlRate.visibility = View.VISIBLE
                rbDtlOne.visibility = View.VISIBLE
                tvReleaseDate.visibility = View.VISIBLE
                tvDtlDate.visibility = View.VISIBLE
                tvOverview.visibility = View.VISIBLE
                tvDtlOverview.visibility = View.VISIBLE
                tvCast.visibility = View.VISIBLE
                rvDtlCast.visibility = View.VISIBLE
                tvTrailer.visibility = View.VISIBLE
                rvDtlTrailer.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}