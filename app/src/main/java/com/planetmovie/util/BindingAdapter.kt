package com.planetmovie.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.planetmovie.R
import com.planetmovie.data.remote.model.MovieCast
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.data.remote.model.MovieVideosResult
import com.planetmovie.ui.movie.MovieFragmentDirections
import com.planetmovie.ui.searchmovie.SearchMovieFragmentDirections
import com.planetmovie.ui.searchtv.SearchTvFragmentDirections
import com.planetmovie.ui.tv.TvFragmentDirections
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
                val action =
                    MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId)
                view.findNavController().navigate(action)

            }
        }

        @BindingAdapter("android:navigateSearchMovieToDetail")
        @JvmStatic
        fun navigateSearchMovieToDetail(view: CardView, movieId: Int) {
            view.setOnClickListener {
                val action =
                    SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(
                        movieId
                    )
                view.findNavController().navigate(action)
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
                val action =
                    SearchTvFragmentDirections.actionSearchTvFragmentToTvDetailFragment(tvId)
                view.findNavController().navigate(action)
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
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:castPoster")
        @JvmStatic
        fun castPoster(imageView: ImageView, movieResult: MovieCast) {
            imageView.load(BASE_IMG_URL_CAST + movieResult.profilePath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("android:trailerPoster")
        @JvmStatic
        fun trailerPoster(imageView: ImageView, trailerResult: MovieVideosResult) {
            imageView.load(BASE_TRAILER_THUMBNAIL + trailerResult.key + BASE_TRAILER_THUMBNAIL_END) {
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

    }
}