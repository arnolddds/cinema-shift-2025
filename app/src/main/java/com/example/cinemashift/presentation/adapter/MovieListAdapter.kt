package com.example.cinemashift.presentation.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemashift.R
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.presentation.screen.movielist.MovieListUiState
import com.google.android.material.button.MaterialButton

class MovieListAdapter(
    private val onMovieClick: (String) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    private var movies: List<Movie> = emptyList()

    fun updateMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(
        view: View,
        private val onMovieClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        private val titleTextView: TextView = view.findViewById(R.id.movieTitleTextView)
        private val ratingBar: RatingBar = view.findViewById(R.id.movieRatingBar)
        private val imageView: ImageView = view.findViewById(R.id.movieImageView)
        private val genreYearTextView: TextView = view.findViewById(R.id.genreYearTextView)
        private val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        private val detailsButton: MaterialButton = view.findViewById(R.id.detailsButton)
        private val movieTypeTextView: TextView = view.findViewById(R.id.movieTypeTextView)

        fun bind(movie: Movie) {
            setTextFields(movie)
            setRating(movie.rating)
            loadImage(movie.imageUrl)
            setupClickListener(movie.id)
        }

        private fun setTextFields(movie: Movie) {
            titleTextView.text = movie.title
            genreYearTextView.text = context.getString(
                R.string.genre_year_format,
                movie.genres.firstOrNull().orEmpty(),
                movie.country.name,
                movie.releaseDate
            )
            ratingTextView.text = context.getString(R.string.kinopoisk_rating_format, movie.rating)
            movieTypeTextView.text = context.getString(R.string.movie_type_film)
        }

        private fun setRating(rating: Float) {
            ratingBar.rating = rating / 2
            val color = getRatingColor(rating)
            ratingBar.progressTintList = ColorStateList.valueOf(color)
            ratingBar.progressBackgroundTintList = ColorStateList.valueOf(color)
        }

        private fun loadImage(imageUrl: String) {
            val baseUrl = context.getString(R.string.base_image_url)
            val fullUrl = if (imageUrl.startsWith("http")) imageUrl else baseUrl + imageUrl

            Glide.with(context)
                .load(fullUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_movie)
                .error(R.drawable.error_movie)
                .into(imageView)
        }

        private fun setupClickListener(movieId: String) {
            detailsButton.setOnClickListener {
                onMovieClick(movieId)
            }
        }

        private fun getRatingColor(rating: Float): Int {
            return when {
                rating >= 7.0f -> ContextCompat.getColor(context, R.color.rating_high)
                rating >= 5.0f -> ContextCompat.getColor(context, R.color.rating_medium)
                else -> ContextCompat.getColor(context, R.color.rating_low)
            }
        }
    }
}

