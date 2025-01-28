package com.example.cinemashift.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemashift.R
import com.example.cinemashift.databinding.ItemMovieBinding
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

        fun bind(movie: MovieUI) {
            titleTextView.text = movie.title
            ratingBar.rating = movie.rating
            genreYearTextView.text = "${movie.genres.firstOrNull() ?: ""} ${movie.country}, ${movie.year}"
            ratingTextView.text = "Kinopoisk - ${movie.rating}"

            val baseUrl = context.getString(R.string.base_image_url)
            Glide.with(context)
                .load(baseUrl + movie.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_movie)
                .error(R.drawable.error_movie)
                .into(imageView)
        }
    }
}