package com.planetmovie.ui.tvdetail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.databinding.FragmentTvDetailBinding
import com.planetmovie.ui.adapter.ItemCastAdapter
import com.planetmovie.ui.adapter.ItemTrailerAdapter
import com.planetmovie.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvDetailFragment : Fragment(R.layout.fragment_tv_detail) {

    // View Binding
    private var _binding: FragmentTvDetailBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mTvDetailViewModel: TvDetailViewModel by viewModels()

    // Adapter
    private val mTvDetailCastAdapter: ItemCastAdapter by lazy { ItemCastAdapter() }
    private val mTvDetailTrailerAdapter: ItemTrailerAdapter by lazy { ItemTrailerAdapter() }

    // Favorite Tv
    private var tvSaved = false
    private var savedTvId = 0

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    // Navigation Args
    private val args by navArgs<TvDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvDetailBinding.bind(view)
        setupRecycler()
        observeViewModel()
        checkSavedFavoriteTv()
    }

    private fun setupRecycler() {
        binding.rvDtlCast.apply {
            adapter = mTvDetailCastAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvDtlTrailer.apply {
            adapter = mTvDetailTrailerAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModel() {
        val tvId: Int = args.tvId
        mTvDetailViewModel.getTvDetail(tvId)
        mTvDetailViewModel.tvDetail.observe(viewLifecycleOwner) { detailTv ->
            when (detailTv) {
                is Resource.Success -> {
                    showShimmer(false)
                    detailTv.data?.let { detailData(it) }
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
        data.let { detailResponse ->
            binding.apply {
                val rating = "${detailResponse.voteAverage} / 10"
                tvDtlDate.text = detailResponse.tvFirstAirDate
                rbDtlOne.rating = detailResponse.voteAverage.div(2).toFloat()
                if (detailResponse.overview.isNotEmpty()) {
                    tvDtlOverview.text = detailResponse.overview
                } else tvDtlOverview.text = getString(R.string.overview_not_available)
                tvDtlRate.text = rating
                ivDtlTvBackdrop.load(Constant.BASE_IMG_URL_BACKDROP + detailResponse.backdropPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                ivDtlPoster.load(Constant.BASE_IMG_URL_POSTER + detailResponse.posterPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                if (detailResponse.movieCredits.movieCast.isNotEmpty()) {
                    mTvDetailCastAdapter.castDiffUtil(detailResponse.movieCredits)
                } else tvCast.visibility = View.GONE
                if (detailResponse.movieVideos.movieVideoResults.isNotEmpty()) {
                    mTvDetailTrailerAdapter.trailerDiffUtil(detailResponse.movieVideos)
                } else tvTrailer.visibility = View.GONE

                /** FAVORITE MOVIE*/
                binding.fabTvDetail.setOnClickListener {
                    tvSaved = if (!tvSaved) {
                        mTvDetailViewModel.insertFavoriteTv(detailResponse)
                        showSnackBarFavorite("Movie Saved.")
                        changeFabColor(R.color.color_icon_favorite)
                        true
                    } else {
                        mTvDetailViewModel.deleteFavoriteTv(detailResponse.id)
                        showSnackBarFavorite("Movie Removed.")
                        changeFabColor(R.color.color_icon_unfavorite)
                        false
                    }
                }
            }
        }
    }

    /** FAVORITE MOVIE*/
    private fun checkSavedFavoriteTv() {
        mTvDetailViewModel.getFavoriteTv.observe(viewLifecycleOwner) { favoriteMoviesEntity ->
            try {
                for (savedMovie in favoriteMoviesEntity) {
                    if (savedMovie.id == args.tvId) {
                        changeFabColor(R.color.color_icon_favorite)
                        savedTvId = savedMovie.id
                        tvSaved = true
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun changeFabColor(color: Int) {
        binding.fabTvDetail.supportImageTintList = ContextCompat.getColorStateList(requireContext(), color)
    }

    private fun showSnackBarFavorite(message: String) {
        Snackbar.make(
            binding.tvDetailLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") { }.show()
    }

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerDetailTv.startShimmer()
                shimmerDetailTv.visibility = View.VISIBLE
                fabTvDetail.visibility = View.GONE
                ivDtlTvBackdrop.visibility = View.GONE
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
                shimmerDetailTv.stopShimmer()
                shimmerDetailTv.visibility = View.GONE
                fabTvDetail.visibility = View.VISIBLE
                ivDtlTvBackdrop.visibility = View.VISIBLE
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