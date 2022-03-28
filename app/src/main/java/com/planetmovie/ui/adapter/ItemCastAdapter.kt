package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieCast
import com.planetmovie.data.remote.model.MovieCredits
import com.planetmovie.databinding.ItemCastBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemCastAdapter : RecyclerView.Adapter<ItemCastAdapter.MyViewHolder>() {

    private var movieCast = emptyList<MovieCast>()

    class MyViewHolder(var binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCastBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val castDetail = movieCast[position]
        holder.binding.tvCast.isSelected = true
        holder.binding.tvCastCharName.isSelected = true
        holder.binding.movieCast = castDetail
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = movieCast.size

    fun castDiffUtil(newData: MovieCredits) {
        val castDiffUtil = NetworkDiffUtil(movieCast, newData.movieCast)
        val diffUtilResult = DiffUtil.calculateDiff(castDiffUtil)
        movieCast = newData.movieCast
        diffUtilResult.dispatchUpdatesTo(this)
    }
}