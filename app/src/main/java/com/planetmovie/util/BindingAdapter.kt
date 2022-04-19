package com.planetmovie.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.data.local.entity.*
import com.planetmovie.data.remote.model.MovieCast
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.data.remote.model.MovieVideosResult
import com.planetmovie.ui.adapter.ItemFavoriteMovieAdapter
import com.planetmovie.ui.adapter.ItemFavoriteTvAdapter
import com.planetmovie.ui.movie.MovieFragmentDirections
import com.planetmovie.ui.searchmovie.SearchMovieFragmentDirections
import com.planetmovie.ui.searchtv.SearchTvFragmentDirections
import com.planetmovie.ui.tv.TvFragmentDirections
import com.planetmovie.util.Constant.Companion.BASE_IMG_URL_BACKDROP
import com.planetmovie.util.Constant.Companion.BASE_IMG_URL_CAST
import com.planetmovie.util.Constant.Companion.BASE_IMG_URL_POSTER
import com.planetmovie.util.Constant.Companion.BASE_TRAILER_THUMBNAIL
import com.planetmovie.util.Constant.Companion.BASE_TRAILER_THUMBNAIL_END
import com.planetmovie.util.Constant.Companion.BASE_TRAILER_URL
import com.planetmovie.util.Constant.Companion.BASE_TRAILER_URL_APP

class BindingAdapter {
    companion object {

        @BindingAdapter("android:navigateMovieToDetail")
        @JvmStatic
        fun navigateMovieToDetail(view: CardView, movieId: Int) {
            view.setOnClickListener {
                val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId)
                view.findNavController().navigate(action)

            }
        }

        @BindingAdapter("android:navigateSearchMovieToDetail")
        @JvmStatic
        fun navigateSearchMovieToDetail(view: CardView, movieId: Int) {
            view.setOnClickListener {
                val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(movieId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:navigateMovieToFavoriteMovie")
        @JvmStatic
        fun navigateMovieToFavoriteMovie(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    val action = MovieFragmentDirections.actionMovieFragmentToFavoriteMovieFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        @BindingAdapter("android:navigateTvToDetail")
        @JvmStatic
        fun navigateTvToDetail(view: CardView, tvId: Int) {
            view.setOnClickListener {
                val action = TvFragmentDirections.actionTvFragmentToTvDetailFragment(tvId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:navigateSearchTvToDetail")
        @JvmStatic
        fun navigateSearchTvToDetail(view: CardView, tvId: Int) {
            view.setOnClickListener {
                val action = SearchTvFragmentDirections.actionSearchTvFragmentToTvDetailFragment(tvId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:navigateTvToFavoriteTv")
        @JvmStatic
        fun navigateTvToFavoriteTv(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    val action = TvFragmentDirections.actionTvFragmentToFavoriteTvFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        @BindingAdapter("android:navigateMovieToSearch")
        @JvmStatic
        fun navigateMovieToSearch(view: ImageView, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    val action = MovieFragmentDirections.actionMovieFragmentToSearchFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        @BindingAdapter("android:navigateTvToSearch")
        @JvmStatic
        fun navigateTvToSearch(view: ImageView, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    val action = TvFragmentDirections.actionTvFragmentToSearchTvFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        @BindingAdapter("android:posterPath")
        @JvmStatic
        fun posterPath(imageView: ImageView, movieResult: MovieResult) {
            imageView.load(BASE_IMG_URL_POSTER + movieResult.posterPath) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:backdropPath")
        @JvmStatic
        fun backdropPath(imageView: ImageView, movieResult: MovieResult) {
            imageView.load(BASE_IMG_URL_BACKDROP + movieResult.backdropPath) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:castPoster")
        @JvmStatic
        fun castPoster(imageView: ImageView, movieResult: MovieCast) {
            imageView.load(BASE_IMG_URL_CAST + movieResult.profilePath) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:trailerPoster")
        @JvmStatic
        fun trailerPoster(imageView: ImageView, trailerResult: MovieVideosResult) {
            imageView.load(BASE_TRAILER_THUMBNAIL + trailerResult.key + BASE_TRAILER_THUMBNAIL_END) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:favMoviePosterPath")
        @JvmStatic
        fun favMoviePosterPath(imageView: ImageView, favMovieEntity: MovieFavoriteEntity) {
            imageView.load(BASE_IMG_URL_POSTER + favMovieEntity.posterPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:favTvPosterPath")
        @JvmStatic
        fun favTvPosterPath(imageView: ImageView, favTvEntity: TvFavoriteEntity) {
            imageView.load(BASE_IMG_URL_POSTER + favTvEntity.posterPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:trailerCLick")
        @JvmStatic
        fun trailerClick(cardView: CardView, trailerResult: MovieVideosResult) {
            cardView.setOnClickListener {
                val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_TRAILER_URL_APP + trailerResult.key))
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_TRAILER_URL + trailerResult.key))
                try {
                    it.context?.startActivity(appIntent)
                } catch (ex: ActivityNotFoundException) {
                    it.context?.startActivity(webIntent)
                }
            }
        }

        @BindingAdapter(
            "android:no_favorite_movie_data",
            "android:favorite_movie_data",
            requireAll = false
        )
        @JvmStatic
        fun setMovieDataAndViewVisibility(
            view: View,
            movieFavoriteEntity: List<MovieFavoriteEntity>?,
            mAdapter: ItemFavoriteMovieAdapter?
        ) {
            if (movieFavoriteEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.newData(movieFavoriteEntity)
                    }
                }
            }
        }

        @BindingAdapter(
            "android:no_favorite_tv_data",
            "android:favorite_tv_data",
            requireAll = false
        )
        @JvmStatic
        fun setFavDataAndViewVisibility(
            view: View,
            tvFavoriteEntity: List<TvFavoriteEntity>?,
            mAdapter: ItemFavoriteTvAdapter?
        ) {
            if (tvFavoriteEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.newData(tvFavoriteEntity)
                    }
                }
            }
        }

        @BindingAdapter(
            "android:movie_now_playing_response",
            "android:movie_popular_response",
            "android:movie_upcoming_response",
            "android:movie_now_playing_entity",
            "android:movie_popular_entity",
            "android:movie_upcoming_entity",
            requireAll = true
        )
        @JvmStatic
        fun movieNoInternetConnection(
            view: View,
            movieNowPlayingResponse: Resource<MovieResponse>?,
            moviePopularResponse: Resource<MovieResponse>?,
            movieUpcomingResponse: Resource<MovieResponse>?,
            movieNowPlayingEntity: List<MovieNowPlayingEntity>?,
            moviePopularEntity: List<MoviePopularEntity>?,
            movieUpcomingEntity: List<MovieUpcomingEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = movieNowPlayingResponse is Resource.Error
                            && movieNowPlayingEntity.isNullOrEmpty()
                            || moviePopularResponse is Resource.Error
                            && moviePopularEntity.isNullOrEmpty()
                            || movieUpcomingResponse is Resource.Error
                            && movieUpcomingEntity.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = movieNowPlayingResponse is Resource.Error
                            && movieNowPlayingEntity.isNullOrEmpty()
                            || moviePopularResponse is Resource.Error
                            && moviePopularEntity.isNullOrEmpty()
                            || movieUpcomingResponse is Resource.Error
                            && movieUpcomingEntity.isNullOrEmpty()
                    view.text = movieNowPlayingResponse?.message.toString()
                }
            }
        }

        @BindingAdapter(
            "android:tv_airing_today_response",
            "android:tv_top_rated_response",
            "android:tv_popular_response",
            "android:tv_airing_today_entity",
            "android:tv_top_rated_entity",
            "android:tv_popular_entity",
            requireAll = true
        )
        @JvmStatic
        fun tvNoInternetConnection(
            view: View,
            tvAiringTodayResponse: Resource<MovieResponse>?,
            tvTopRatedResponse: Resource<MovieResponse>?,
            tvPopularResponse: Resource<MovieResponse>?,
            tvAiringTodayEntity: List<TvAiringTodayEntity>?,
            tvTopRatedEntity: List<TvTopRatedEntity>?,
            tvPopularEntity: List<TvPopularEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = tvAiringTodayResponse is Resource.Error
                            && tvAiringTodayEntity.isNullOrEmpty()
                            || tvTopRatedResponse is Resource.Error
                            && tvTopRatedEntity.isNullOrEmpty()
                            || tvPopularResponse is Resource.Error
                            && tvPopularEntity.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = tvAiringTodayResponse is Resource.Error
                            && tvAiringTodayEntity.isNullOrEmpty()
                            || tvTopRatedResponse is Resource.Error
                            && tvTopRatedEntity.isNullOrEmpty()
                            || tvPopularResponse is Resource.Error
                            && tvPopularEntity.isNullOrEmpty()
                    view.text = tvAiringTodayResponse?.message.toString()
                }
            }
        }

    }
}