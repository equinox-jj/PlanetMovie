package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListMovieBinding
import com.planetmovie.util.NetworkDiffUtil
import okhttp3.internal.immutableListOf

class ItemMovieListAdapter : RecyclerView.Adapter<ItemMovieListAdapter.MyViewHolder>() {

    private var movieResult = immutableListOf<MovieResult>()

    class MyViewHolder(private val binding: ItemListMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult: MovieResult) {
            binding.movieResult = movieResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMovie = movieResult[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int = movieResult.size

    fun movieDiffUtil(newData: MovieResponse) {
        val moviesDiffUtil = NetworkDiffUtil(movieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}