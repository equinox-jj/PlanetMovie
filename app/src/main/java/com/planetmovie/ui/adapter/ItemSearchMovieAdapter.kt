package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemSearchMovieBinding
import com.planetmovie.util.NetworkDiffUtil
import okhttp3.internal.immutableListOf

class ItemSearchMovieAdapter : RecyclerView.Adapter<ItemSearchMovieAdapter.MyViewHolder>() {

    private var searchMovieResult = immutableListOf<MovieResult>()

    class MyViewHolder(private val binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(searchMovieResult: MovieResult) {
                binding.searchMovieResult = searchMovieResult
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearchMovie = searchMovieResult[position]
        holder.bind(currentSearchMovie)
    }

    override fun getItemCount(): Int = searchMovieResult.size

    fun searchDiffUtil(newData: MovieResponse) {
        val searchMovieDiffUtil = NetworkDiffUtil(searchMovieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchMovieDiffUtil)
        searchMovieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}