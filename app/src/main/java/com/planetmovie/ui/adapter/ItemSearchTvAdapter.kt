package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemSearchTvBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemSearchTvAdapter : RecyclerView.Adapter<ItemSearchTvAdapter.MyViewHolder>() {

    private var searchTvResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemSearchTvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchTvBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearchTv = searchTvResult[position]
        holder.binding.searchTvResult = currentSearchTv
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = searchTvResult.size

    fun searchDiffUtil(newData: MovieResponse) {
        val searchTvDiffUtil = NetworkDiffUtil(searchTvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchTvDiffUtil)
        searchTvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}