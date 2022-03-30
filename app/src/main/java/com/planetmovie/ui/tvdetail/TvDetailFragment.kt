package com.planetmovie.ui.tvdetail

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

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    // Navigation Args
    private val args by navArgs<TvDetailFragmentArgs>()

    // Favorite Menu
    private lateinit var favMenuItem: MenuItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvDetailBinding.bind(view)
        setupRecycler()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.rvDtlCast.apply {
            adapter = mTvDetailCastAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvDtlTrailer.apply {
            adapter = mTvDetailTrailerAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        data.let {
            binding.apply {
                val rating = "${it.voteAverage} / 10"
                tvDtlDate.text = it.tvFirstAirDate
                rbDtlOne.rating = it.voteAverage.div(2).toFloat()
                if (it.overview.isNotEmpty()) {
                    tvDtlOverview.text = it.overview
                } else tvDtlOverview.text = getString(R.string.overview_not_available)
                tvDtlRate.text = rating
                ivDtlTvBackdrop.load(Constant.BASE_IMG_URL_BACKDROP + it.backdropPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                ivDtlPoster.load(Constant.BASE_IMG_URL_POSTER + it.posterPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                if (it.movieCredits.movieCast.isNotEmpty()) {
                    mTvDetailCastAdapter.castDiffUtil(it.movieCredits)
                } else tvCast.visibility = View.GONE
                if (it.movieVideos.movieVideoResults.isNotEmpty()) {
                    mTvDetailTrailerAdapter.trailerDiffUtil(it.movieVideos)
                } else tvTrailer.visibility = View.GONE
            }
        }
    }

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerDetailTv.startShimmer()
                shimmerDetailTv.visibility = View.VISIBLE
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