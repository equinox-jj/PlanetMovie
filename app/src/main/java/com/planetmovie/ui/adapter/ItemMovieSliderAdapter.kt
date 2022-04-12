package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListMovieSliderBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemMovieSliderAdapter : CardSliderAdapter<ItemMovieSliderAdapter.MovieSlideViewHolder>() {

    private var movieResult = emptyList<MovieResult>()

    class MovieSlideViewHolder(var binding: ItemListMovieSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult: MovieResult) {
            binding.movieResult = movieResult
            binding.executePendingBindings()
        }
    }

    override fun bindVH(holder: MovieSlideViewHolder, position: Int) {
        val currentMovieSlider = movieResult[position]
        holder.bind(currentMovieSlider)
    }

    override fun getItemCount(): Int = movieResult.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSlideViewHolder {
        val binding =
            ItemListMovieSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieSlideViewHolder(binding)
    }

    fun movieDiffUtil(newData: MovieResponse) {
        val moviesDiffUtil = NetworkDiffUtil(movieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}