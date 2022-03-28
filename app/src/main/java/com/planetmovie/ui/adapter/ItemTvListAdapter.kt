package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListTvBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemTvListAdapter : RecyclerView.Adapter<ItemTvListAdapter.MyViewHolder>() {

    private var tvResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemListTvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListTvBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTv = tvResult[position]
        holder.binding.tvResult = currentTv
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = tvResult.size

    fun tvDiffUtil(newData: MovieResponse) {
        val tvDiffUtil = NetworkDiffUtil(tvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(tvDiffUtil)
        tvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}