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

class ItemMovieSliderAdapter : CardSliderAdapter<ItemMovieSliderAdapter.MyViewHolder>() {

    private var movieResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemListMovieSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMovieSliderBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun bindVH(holder: MyViewHolder, position: Int) {
        val currentMovieSlider = movieResult[position]
        holder.binding.movieResult = currentMovieSlider
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = movieResult.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    fun movieDiffUtil(newData: MovieResponse) {
        val moviesDiffUtil = NetworkDiffUtil(movieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}