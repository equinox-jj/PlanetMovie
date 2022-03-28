package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListMovieBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemMovieListAdapter : RecyclerView.Adapter<ItemMovieListAdapter.MyViewHolder>() {

    private var movieResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemListMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListMovieBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = movieResult[position]
        holder.binding.movieResult = currentMovie
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = movieResult.size

    fun movieDiffUtil(newData: MovieResponse) {
        val moviesDiffUtil = NetworkDiffUtil(movieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}