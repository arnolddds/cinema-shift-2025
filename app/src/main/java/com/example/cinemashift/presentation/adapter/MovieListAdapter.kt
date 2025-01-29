package com.example.cinemashift.presentation.adapter

import android.content.res.ColorStateList
import android.util.Log
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
import com.example.cinemashift.presentation.model.MovieUI
import com.google.android.material.button.MaterialButton

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    private val movies = mutableListOf<MovieUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun setMovies(newMovies: List<MovieUI>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.movieTitleTextView)
        private val ratingBar: RatingBar = view.findViewById(R.id.movieRatingBar)
        private val imageView: ImageView = view.findViewById(R.id.movieImageView)
        private val genreYearTextView: TextView = view.findViewById(R.id.genreYearTextView)
        private val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        private val detailsButton: MaterialButton = view.findViewById(R.id.detailsButton)
        private val context = view.context
        private val movieTypeTextView: TextView = view.findViewById(R.id.movieTypeTextView)

        private fun getRatingColor(rating: Float): Int {
            return when {
                rating >= 7.0f -> ContextCompat.getColor(context, R.color.rating_high)
                rating >= 5.0f -> ContextCompat.getColor(context, R.color.rating_medium)
                else -> ContextCompat.getColor(context, R.color.rating_low)
            }
        }

        fun bind(movie: MovieUI) {
            titleTextView.text = movie.title
            ratingBar.rating = movie.rating / 2
            genreYearTextView.text = "${movie.genres.firstOrNull() ?: ""} ${movie.country}, ${movie.year}"
            ratingTextView.text = "Kinopoisk - ${movie.rating}"
            movieTypeTextView.text = "Фильм"

            ratingBar.progressTintList = ColorStateList.valueOf(getRatingColor(movie.rating))
            ratingBar.progressBackgroundTintList = ColorStateList.valueOf(getRatingColor(movie.rating))

            val baseUrl = context.getString(R.string.base_image_url)
            val imageUrl = if (movie.imageUrl.startsWith("http")) {
                movie.imageUrl
            } else {
                baseUrl + movie.imageUrl
            }

            Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_movie)
                .error(R.drawable.error_movie)
                .into(imageView)



            detailsButton.setOnClickListener {
                // TODO: Обработка клика по кнопке Подробнее
            }
        }
    }
}